/**
 * pageload
 */
const baseUrl = 'http://localhost:8081'
var jwt = null
//检查登录状态
isAlreadyLogin()

/**
 * function
 */
//检查登录状态
function isAlreadyLogin() {
    jwt = localStorage.getItem("jwt")
    if (jwt === null) {
        jwt = sessionStorage.getItem("jwt")
    }
    if (jwt !== null) {
        window.location.href = 'user.html'
    }
}

//用户登录
async function login() {
    var requestUrl = baseUrl + '/user/login'
    var requestParams = new URLSearchParams()
    requestParams.append('userName', document.getElementById('usernameInput').value)
    requestParams.append('password', document.getElementById('passwordInput').value)
    if (document.getElementById("autoLogin").checked === true) {
        requestParams.append('autoLogin', 3 * 24 * 60)
    }
    var request = new Request(requestUrl, {
        method: 'POST',
        body: requestParams
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())

    if (responseJSON.code === true) {
        //放入本地存储中，这样即使刷新页面也不会丢失其中的数据
        localStorage.setItem('jwt', responseJSON.data)
        //放入会话存储中，重新打开需要重新登录
        //sessionStorage.setItem('jwt',responseJSON.data)
        //跳转页面
        window.location.href = 'user.html'

        /**
         * only for debug
         */
        sessionStorage.setItem("msg", "登录成功")
    } else {
        console.log(responseJSON)
    }
}