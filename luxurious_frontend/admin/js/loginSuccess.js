$(document).ready(function () {
    let token = localStorage.getItem('jwt');
    //var jwtJson = parseJwt(token);
    if (token) {
         let jwtJson = parseJwt(token);

         let userName = jwtJson.sub;         
        let userEmail = jwtJson.email;      

         document.getElementById("userName").textContent = userName;
        document.getElementById("userEmail").textContent = userEmail;
    } else {
        
        console.log("Token không tồn tại.");
    }

});


function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}