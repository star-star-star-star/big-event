/**
 * page load
 */
const baseUrl = 'http://localhost:8081'
var jwt = null
var articleStatus = null
var select = document.getElementById('categoryInput')
//检查登录状态
isAlreadyLogin()
//加载用户名和用户头像
pageLoad()
//加载文章分类列表
categorylistLoad()

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
        window.location.href = "../uder/login.html"
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
async function categorylistLoad(){
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
        for (var i = 0; i < responseJSON.data.length; i++) {
            addOption(responseJSON.data[i].categoryName)
        }
    } else {
        window.alert('获取文章分类列表失败')
    }
}

//动态添加文章分类列表
function addOption(name){
    var option = document.createElement('option')
    option.value = name
    option.label = name
    select.appendChild(option)
}

//保存文章
function save(){
    articleStatus = '草稿'
    upload()
}

//发布文章
function publish(){
    articleStatus = '已发布'
    upload()
}

//上传文章信息
async function upload(){
    var requestUrl = baseUrl + '/article/addArticle'
    var header = new Headers()
    header.append('authorization',jwt)
    var formdata = new FormData()
    var file = document.getElementById('picInput')
    formdata.append('pic',file.files[0])
    formdata.append('title',document.getElementById('titleInput').value)
    formdata.append('content',document.getElementById('contentInput').value)
    formdata.append('status',articleStatus)
    formdata.append('categoryName',select.value)
    var request = new Request(requestUrl,{
        method:'POST',
        headers:header,
        body:formdata
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if(responseJSON.code === true){
        window.alert('文章添加成功')
        window.location.href = 'addarticle.html'
    }else{
        window.alert('文章添加失败')
        console.log(responseJSON)
    }
}

//退出登录
function logout() {
    //退出登录，消除jwt
    localStorage.removeItem("jwt")
    sessionStorage.removeItem("jwt")
    window.location.href = '../index.html'
}