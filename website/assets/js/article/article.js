/**
 * page load
 */
const baseUrl = 'http://localhost:8081'
var jwt = null
var startIndex = null   //开始序号
var endIndex = null //结束序号
var page = null    //当前页数
const recordsPerPage = 6 //每一个最大记录数
var table = document.getElementById('articleTable')
var isEndPage = null    //是否为最后一页
var isFirstPage = null  //是否为第一页
//加载初始化参数
if (sessionStorage.getItem('page') === null) {
    sessionStorage.setItem('page', '1')
}
if (sessionStorage.getItem('startIndex') === null) {
    sessionStorage.setItem('startIndex', '1')
}
if (sessionStorage.getItem('isFirstPage') === null) {
    sessionStorage.setItem('isFirstPage', '1')
}
//检查登录状态
isAlreadyLogin()
//加载用户名和用户头像
pageLoad()
//加载文章列表
articleLoad()
/**
 * function
 */

//检查登录状态
function isAlreadyLogin() {
    jwt = localStorage.getItem("jwt")
    if (jwt === null) {
        jwt = sessionStorage.getItem("jwt")
    }
    if (jwt === null) {
        window.alert("登录过期，请重新登录")
        window.location.href = "../user/login.html"
    }
}

//加载用户名和用户头像
async function pageLoad() {
    var requestUrl = baseUrl + '/user/userInfo'
    var header = new Headers()
    header.append('authorization', jwt)
    var request = new Request(requestUrl, {
        method: 'GET',
        headers: header
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if (responseJSON.code === true) {
        document.getElementById('userPic').src = responseJSON.data.pic
        document.getElementById('usernameLabel').innerText = responseJSON.data.userName
    } else {
        window.alert('获取用户信息失败')
    }
}

//获取文章列表
async function articleLoad() {
    var requestUrl = baseUrl + '/article/getArticle'
    var header = new Headers()
    header.append('authorization', jwt)
    var requestParams = new URLSearchParams()
    requestParams.append('pageNum', sessionStorage.getItem('page'))
    requestParams.append('pageSize', recordsPerPage)
    var request = new Request(requestUrl, {
        method: 'POST',
        headers: header,
        body: requestParams
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    console.log(responseJSON)
    if (responseJSON.code === true) {
        if (responseJSON.data.num == 0) {
            //当前是最后一页
            sessionStorage.setItem('isEndPage', '1')
            window.alert('当前已经是最后一页了')
            page = sessionStorage.getItem('page')
            page--
            sessionStorage.setItem('page', page)
            sessionStorage.setItem('startIndex',recordsPerPage*(page-1)+1)
            articleLoad()
        } else {
            //当前不是最后一页
            sessionStorage.setItem('isEndPage', '0')
        }
        if (sessionStorage.getItem('page') === '1') {
            //当前是第一页
            sessionStorage.setItem('isFirstPage', '1')
        } else{
            //当前不是第一页
            sessionStorage.setItem('isFirstPage', '0')
        }
        startIndex = sessionStorage.getItem('startIndex')
        for (var i = 0; i < responseJSON.data.num; i++, startIndex++) {
            addTableRow(
                startIndex,
                responseJSON.data.records[i].article.coverImg,
                responseJSON.data.records[i].article.title,
                responseJSON.data.records[i].article.content,
                responseJSON.data.records[i].article.status,
                responseJSON.data.records[i].categoryName,
                responseJSON.data.records[i].article.createTime,
                responseJSON.data.records[i].article.updateTime
            )
        }
        sessionStorage.setItem('endIndex', startIndex)
    } else {
        window.alert('获取文章列表失败')
    }
}

//动态添加表格(序号，文章封面，文章标题，文章内容摘要，文章状态，文章所属分类，创建时间，更新时间)
function addTableRow(id, coverImg, title, content, status, category, createTime, updateTime) {
    /**
     * 行
     */
    var row = document.createElement('tr')

    /**
     * 单元格
     */
    //序号
    var cell1 = document.createElement('td')
    var textNode1 = document.createTextNode(id)
    cell1.appendChild(textNode1)
    //文章封面
    var cell2 = document.createElement('td')
    var imgNode1 = document.createElement('img')
    imgNode1.src = coverImg
    imgNode1.width = 50
    imgNode1.height = 50
    cell2.appendChild(imgNode1)
    //文章标题
    var cell3 = document.createElement('td')
    var textNode2 = document.createTextNode(title)
    cell3.appendChild(textNode2)
    //文章内容摘要
    var cell4 = document.createElement('td')
    var textNode3 = document.createTextNode(content)
    cell4.appendChild(textNode3)
    //文章状态
    var cell5 = document.createElement('td')
    var textNode4 = document.createTextNode(status)
    cell5.appendChild(textNode4)
    //文章所属分类
    var cell6 = document.createElement('td')
    var textNode5 = document.createTextNode(category)
    cell6.appendChild(textNode5)
    //创建时间
    var cell7 = document.createElement('td')
    var textNode6 = document.createTextNode(createTime)
    cell7.appendChild(textNode6)
    //更新时间
    var cell8 = document.createElement('td')
    var textNode7 = document.createTextNode(updateTime)
    cell8.appendChild(textNode7)
    //更新文章按钮
    var cell9 = document.createElement('td')
    var button1 = document.createElement('button')
    button1.innerText = '⬆'
    button1.setAttribute('data-bs-toggle', 'tooltip')
    button1.setAttribute('title', '修改该条文章')
    button1.addEventListener('click', () => {
        sessionStorage.setItem('articleName', title)
        window.location.href = 'updatearticle.html'
    })
    cell9.appendChild(button1)
    //删除文章按钮
    var cell10 = document.createElement('td')
    var button2 = document.createElement('button')
    button2.innerText = '×'
    button2.setAttribute('data-bs-toggle', 'tooltip')
    button2.setAttribute('title', '删除该条文章')
    button2.addEventListener('click', () => {
        sessionStorage.setItem('articleName', title)
        deleteArticle()
    })
    cell10.appendChild(button2)

    /**
     * 将单元格放入行中
     */
    row.appendChild(cell1)
    row.appendChild(cell2)
    row.appendChild(cell3)
    row.appendChild(cell4)
    row.appendChild(cell5)
    row.appendChild(cell6)
    row.appendChild(cell7)
    row.appendChild(cell8)
    row.appendChild(cell9)
    row.appendChild(cell10)

    /**
     * 将行放入表格中
     */
    table.appendChild(row)
}

//删除文章
async function deleteArticle() {

}

//退出登录
function logout() {
    //退出登录，消除jwt
    localStorage.removeItem("jwt")
    sessionStorage.removeItem("jwt")
    window.location.href = '../index.html'
}

//上一页
function toPrePage() {
    if (sessionStorage.getItem('isFirstPage') === '1') {
        window.alert('当前已经是第一页了')
    } else {
        //当前不是第一页
        page = sessionStorage.getItem('page')
        page--
        sessionStorage.setItem('page', page)
        startIndex = sessionStorage.getItem('startIndex')
        endIndex = sessionStorage.getItem('endIndex')
        sessionStorage.setItem('startIndex',startIndex-recordsPerPage)
        window.location.href = 'article.html'
    }
}

//下一页
function toNextPage() {
    if (sessionStorage.getItem('isEndPage') === '1') {

    } else {
        page = sessionStorage.getItem('page')
        page++
        sessionStorage.setItem('page', page)
        endIndex = sessionStorage.getItem('endIndex')
        sessionStorage.setItem('startIndex',endIndex)
        window.location.href = 'article.html'
    }
}