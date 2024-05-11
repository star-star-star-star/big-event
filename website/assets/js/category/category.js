/**
 * page load
 */
const baseUrl = 'http://localhost:8081'
var jwt = null
var table = document.getElementById('categoryTable')
var index = 1

//检查登录状态
isAlreadyLogin()

//加载用户名和用户头像
pageLoad()
//加载文章分类列表
categoryLoad()

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

//加载文章分类列表
async function categoryLoad() {
    var requestUrl = baseUrl + '/category/getCategories'
    var header = new Headers()
    header.append('authorization', jwt)
    var request = new Request(requestUrl, {
        method: 'GET',
        headers: header
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if (responseJSON.code === true) {
        console.log(responseJSON)
        for (var i = 0; i < responseJSON.data.length; i++, index++) {
            addTableRow(
                index,
                responseJSON.data[i].categoryName,
                responseJSON.data[i].categoryAlias,
                responseJSON.data[i].createTime,
                responseJSON.data[i].updateTime
            )
        }
    } else {
        window.alert('获取文章分类列表失败')
    }
}

//动态添加表格(序号，文章分类名称，文章分类别名，创建时间，更新时间)
function addTableRow(id, name, alias, createTime, updateTime) {
    var row = document.createElement('tr')
    var cell1 = document.createElement('td')
    var cell2 = document.createElement('td')
    var cell3 = document.createElement('td')
    var cell4 = document.createElement('td')
    var cell5 = document.createElement('td')
    var cell6 = document.createElement('td')    //修改文章分类信息
    var cell7 = document.createElement('td')    //删除文章分类信息
    var textNode1 = document.createTextNode(id)
    var textNode2 = document.createTextNode(name)
    var textNode3 = document.createTextNode(alias)
    var textNode4 = document.createTextNode(createTime)
    var textNode5 = document.createTextNode(updateTime)
    var button1 = document.createElement('button')
    var button2 = document.createElement('button')
    cell1.appendChild(textNode1)
    cell2.appendChild(textNode2)
    cell3.appendChild(textNode3)
    cell4.appendChild(textNode4)
    cell5.appendChild(textNode5)
    button1.innerText = '⬆'
    button1.setAttribute('data-bs-toggle', 'tooltip')
    button1.setAttribute('title', '修改该条文章分类')
    button1.addEventListener('click', () => {
        sessionStorage.setItem('categoryName', name)
        window.location.href = 'updatecategory.html'
    })
    button2.innerText = '×'
    button2.setAttribute('data-bs-toggle', 'tooltip')
    button2.setAttribute('title', '删除该条文章分类')
    button2.addEventListener('click', () => {
        sessionStorage.setItem('categoryName', name)
        deleteCategory()
    })
    cell6.appendChild(button1)
    cell7.appendChild(button2)
    row.appendChild(cell1)
    row.appendChild(cell2)
    row.appendChild(cell3)
    row.appendChild(cell4)
    row.appendChild(cell5)
    row.appendChild(cell6)
    row.appendChild(cell7)
    table.appendChild(row)
}

//删除文章分类
async function deleteCategory() {
    var requestUrl = baseUrl + '/category/deleteCategory/' + sessionStorage.getItem('categoryName')
    var header = new Headers()
    header.append('authorization', jwt)
    var request = new Request(requestUrl, {
        method: 'DELETE',
        headers: header
    })
    await fetch(request)
    window.alert('文章分类删除成功')
    window.location.href = 'category.html'
}

//退出登录
function logout() {
    //退出登录，消除jwt
    localStorage.removeItem("jwt")
    sessionStorage.removeItem("jwt")
    window.location.href = '../index.html'
}