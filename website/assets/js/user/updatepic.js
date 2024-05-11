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

//上传文件
async function uploadpic(){
    var requestUrl = baseUrl + '/user/updateUserPicture'
    var header = new Headers()
    header.append('authorization',jwt)
    var formdata = new FormData()
    var file = document.getElementById('picInput')
    formdata.append('pic',file.files[0])
    var request = new Request(requestUrl,{
        method:'PUT',
        headers:header,
        body:formdata
    })
    var response = await fetch(request)
    var responseJSON = JSON.parse(await response.text())
    if(responseJSON.code === true){
        window.location.href = 'user.html'
    }else{
        console.log(responseJSON)
    }
}