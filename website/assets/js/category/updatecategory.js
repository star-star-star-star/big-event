/**
 * page load
 */
const baseUrl = 'http://localhost:8081'
var jwt = null
//检查登录状态
isAlreadyLogin()

//加载用户名和用户头像
pageLoad()

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

//更新文章分类
async function updateCategory() {
    var name = sessionStorage.getItem('categoryName')
    var requestUrl = baseUrl + '/category/updateCategory/' + name + ',' + document.getElementById('newName').value + ',' + document.getElementById('newAlias').value
    var header = new Headers()
    header.append('authorization', jwt)
    var request = new Request(requestUrl, {
        method: 'PUT',
        headers: header
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if (responseJSON.code === true) {
        window.location.href = 'category.html'
    } else {
        window.alert('更新文章失败')
    }
}

//退出登录
function logout() {
    //退出登录，消除jwt
    localStorage.removeItem("jwt")
    sessionStorage.removeItem("jwt")
    window.location.href = '../index.html'
}