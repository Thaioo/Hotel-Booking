
$(document).ready(function () {
    
    //checkTokenAndRedirect();
    localStorage.removeItem('jwt');  


    $(".login-form").submit(function (e) {
        e.preventDefault();
        let username = $("#input_username").val();
        let password = $("#input_password").val();

        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: "http://localhost:9999/authen/login",
            data: JSON.stringify({ "username": username, "password": password }),
            success: function (response) {

                if (response.statusCode == 200) {

                    if (response.message.includes("User not")) {
                        alert("Username does not exist")
                    } else if (response.message.includes("Incorrect")) {
                        alert("Incorrect password!")
                    } else {
                        var jwtJson = parseJwt(response.data)
                        localStorage.setItem('jwt', response.data)
                        localStorage.setItem('username', jwtJson.sub)
                        localStorage.setItem('expTime', jwtJson.exp)
                         
                    let redirectUrl = getItemWithExpiry('redirectUrl');
                    //localStorage.getItem('redirectUrl');

                    if (redirectUrl) {
                        localStorage.removeItem('redirectUrl');  
                        window.location.href = redirectUrl;  
                    } else {
                        window.location.href = "index.html";  
                    }
                        //window.location.href = "index.html"
                    }
                }
            },
            error: function (response) {
                console.log(response)
                alert(response.responseJSON.message)
            }
        });
    });

});

function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}


// function checkTokenAndRedirect() {
//     // Kiểm tra xem có token trong localStorage hay không
//     let token = localStorage.getItem('jwt');
//     let savedUrl = localStorage.getItem('URL');

//     // Nếu có token và url đã lưu trong localStorage, thì chuyển hướng
//     if (token) {
//         window.location.href = "index.html";
//     }
// }


function getItemWithExpiry(key) {
    const itemStr = localStorage.getItem(key);

     if (!itemStr) {
        return null;
    }

    const item = JSON.parse(itemStr);  

    const now = new Date();

    // Kiểm tra nếu thời gian hiện tại đã quá hạn sử dụng
    if (now.getTime() > item.expiry) {
        // Xóa giá trị đã hết hạn khỏi localStorage
        localStorage.removeItem(key);
        return null;
    }

    return item.value;  
}