/**
 * page load
 */
const baseUrl = 'http://localhost:8081'
var jwt = null
var clickCounterForNickname = 0
var clickCounterForEmail = 0

//检查登录状态
isAlreadyLogin()

//加载用户信息
getUserInfo()

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

//退出登录
function logout() {
    //退出登录，消除jwt
    localStorage.removeItem("jwt")
    sessionStorage.removeItem("jwt")
    window.location.href = '../index.html'
}

//获取用户信息
async function getUserInfo(){
    var requestUrl = baseUrl + '/user/userInfo'
    var header = new Headers()
    header.append('authorization',jwt)
    var request = new Request(requestUrl,{
        method:'GET',
        headers:header
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if(responseJSON.code === true){
        /**
         * 加载用户图片
         */
        var userPic = document.getElementById('userPic')
        userPic.src = responseJSON.data.pic
        /**
         * 欢迎信息
         */
        document.getElementById('usernameLabel').innerText = '欢迎你：' + responseJSON.data.userName
        /**
         * 加载用户信息
         */
        document.getElementById('userid').innerText = responseJSON.data.id
        document.getElementById('username').innerText = responseJSON.data.userName
        document.getElementById('usernickname').value = responseJSON.data.nickName
        document.getElementById('userpic').src = responseJSON.data.pic
        document.getElementById('useremail').value = responseJSON.data.email
        document.getElementById('createtime').innerText = responseJSON.data.createTime
        document.getElementById('updatetime').innerText = responseJSON.data.updateTime
    }else{
        window.alert("获取用户信息失败")
    }
}

//修改用户昵称
async function updatenickname(){
    clickCounterForNickname++
    if(clickCounterForNickname % 2 == 1){
        document.getElementById('usernickname').removeAttribute('readonly')
    }else{
        var requestUrl = baseUrl + '/user/updateNickname'
        var header = new Headers()
        header.append('authorization',jwt)
        var requestParams = new URLSearchParams()
        requestParams.append('nickname',document.getElementById('usernickname').value)
        var request = new Request(requestUrl,{
            method:'POST',
            headers:header,
            body:requestParams
        })
        var response = await fetch(request)
        var responseJSON = JSON.parse(await response.text())
    
        console.log(responseJSON)
        if(responseJSON.code === true){
            window.alert('用户昵称更新成功')
            getUserInfo()
        }else{
            window.alert('用户昵称更新失败')
        }
    }
    
}

//修改用户邮箱
async function updateemail(){
    clickCounterForEmail++
    if(clickCounterForEmail % 2 == 1){
        document.getElementById('useremail').removeAttribute('readonly')
    }else{
        var requestUrl = baseUrl + '/user/updateEmail'
        var header = new Headers()
        header.append('authorization',jwt)
        var requestParams = new URLSearchParams()
        requestParams.append('email',document.getElementById('useremail').value)
        var request = new Request(requestUrl,{
            method:'POST',
            headers:header,
            body:requestParams
        })
        var response = await fetch(request)
        var responseJSON = JSON.parse(await response.text())
        if(responseJSON.code === true){
            window.alert('用户邮箱更新成功')
            getUserInfo()
        }else{
            window.alert('用户邮箱更新失败')
        }
    }
    
}