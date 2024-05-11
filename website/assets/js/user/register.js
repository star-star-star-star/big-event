/**
 * page load
 */


/**
 * function
 */
//用户注册
async function register(){
    var requestUrl = baseUrl + '/user/register'
    var requestParams = new URLSearchParams()
    requestParams.append('userName',document.getElementById('usernameInput1').value)
    requestParams.append('password',document.getElementById('passwordInput1').value)
    var request = new Request(requestUrl,{
        method:'POST',
        body:requestParams
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if(responseJSON.code === true){
        loginWithParam(document.getElementById('usernameInput1').value,document.getElementById('passwordInput1').value)
    }else{
        window.alert('注册失败'+responseJSON.data)
    }
}

//用户登录
async function loginWithParam(username,password){
    var requestUrl = baseUrl + '/user/login'
    var requestParams = new URLSearchParams()
    requestParams.append('userName',username)
    requestParams.append('password',password)
    var request = new Request(requestUrl,{
        method:'POST',
        body:requestParams
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    
    if(responseJSON.code === true){
        //放入本地存储中，这样即使刷新页面也不会丢失其中的数据
        //localStorage.setItem('jwt',jwt)
        //放入会话存储中，重新打开需要重新登录
        sessionStorage.setItem('jwt',responseJSON.data)
        localStorage.removeItem("jwt")
        //跳转页面
        window.location.href = 'user.html'
        window.alert('注册成功')
    }else{
        window.alert('注册失败'+responseJSON.data)
    }
    
}