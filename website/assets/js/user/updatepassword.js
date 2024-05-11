/**
 * page load
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
    if (jwt === null) {
        window.alert("登录过期，请重新登录")
        window.location.href = "login.html"
    }
}

//重置密码
async function resetpassword(){
    var requestUrl = baseUrl + '/user/changePassword'
    var header = new Headers()
        header.append('authorization',jwt)
        var requestParams = new URLSearchParams()
        requestParams.append('newPassword',document.getElementById('passwordInput').value)
        requestParams.append('confirmation',document.getElementById('passwordInput2').value)
        var request = new Request(requestUrl,{
            method:'PUT',
            headers:header,
            body:requestParams
        })
        var response = await fetch(request)
        var responseJSON = JSON.parse(await response.text())
        if(responseJSON.code === true){
            //更新jwt
            jwt = localStorage.getItem("jwt")
            if(jwt !== null){
                //更新本地存储的jwt
                localStorage.setItem('jwt',responseJSON.data)
            }
            jwt = sessionStorage.getItem('jwt')
            if(jwt !== null){
                //更新会话存储的jwt
                sessionStorage.setItem('jwt',responseJSON.data)
            }
            //返回用户首页
            window.location.href = 'user.html'
        }else{
            window.alert('密码重置失败')
        }
}