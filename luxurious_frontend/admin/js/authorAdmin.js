$(document).ready(function () {
    let token = localStorage.getItem('jwt');
    console.log(token)

    // Kiểm tra xem token có tồn tại không
    if (!token) {
        //localStorage.setItem('redirectUrl', window.location.href); 
        setItemWithExpiry('redirectUrl', window.location.href,  60 * 1000);
        window.location.href = "signin.html"; // Chuyển đến trang đăng nhập nếu không có token
        alert("Bạn không có quyền truy cập trang này.");
    } else {


        if (isTokenExpired(token)) {
            alert("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");// in ra thôg báo là đã hết hạn token
            localStorage.removeItem('jwt'); // Xóa token 
            window.location.href = "signin.html"; // Chuyển đến trang đăng nhập khi token bị hết hạn 
        } else {


            

            var jwtJson = parseJwt(token);


            if(window.location.href.includes("guest") || window.location.href.includes("Employee")||window.location.href.includes("team")){
                    if(jwtJson.role !== "ROLE_ADMIN"){
                        alert("Bạn không có quyền truy cập trang hoặc thực hiện chức năng này.");
                        //localStorage.removeItem('jwt');  
            
                        window.location.href = "401-author-page.html"; // Chuyển đến trang thông báo không có quyền
            
                        //localStorage.setItem('redirectUrl', window.location.href); 
                        setItemWithExpiry('redirectUrl', window.location.href,  60 * 1000);
                    }
            }else{
                if(jwtJson.role !== "ROLE_ADMIN"&&jwtJson.role !== "ROLE_HOTEL_MANAGER"&&jwtJson.role !== "ROLE_STAFF"&&jwtJson.role !== "ROLE_GUEST"&&jwtJson.role !== "ROLE_HOTEL_MANAGER"&&jwtJson.role !== "ROLE_RES_MANAGER"){
                    alert("Bạn không có quyền truy cập trang hoặc thực hiện chức năng này.");
                    //localStorage.removeItem('jwt');  
        
                    window.location.href = "401-author-page.html"; // Chuyển đến trang thông báo không có quyền
        
                    //localStorage.setItem('redirectUrl', window.location.href); 
                    setItemWithExpiry('redirectUrl', window.location.href,  60 * 1000);
                }
            }

            //let jwtJson = parseJwt(token);

            console.log(jwtJson)

         let userName = jwtJson.sub;         
        let userEmail = jwtJson.email;     
        let avatarUrl =  jwtJson.avatar;

        if (avatarUrl === 'http://localhost:9999/file/null'||avatarUrl === 'http://localhost:9999/file/') {
            avatarUrl = '/luxurious_frontend/assets/img/logo/dribbble.png'; // Đường dẫn đến hình mặc định
        }
 

       
         document.getElementById("userName").textContent = userName;
        document.getElementById("userEmail").textContent = userEmail;
        if (avatarUrl.slice(-4) != 'null') {
            document.getElementById("conmeo").src = avatarUrl;
        } else {
            document.getElementById("conmeo").src = 'assets/img/user/thumb.jpg';
        }
        //document.querySelector('.user').setAttribute('src', avatarUrl);
        }


       
        
        // // Kiểm tra quyền người dùng
        // if (jwtJson.role !== "ROLE_ADMIN") { // Giả sử 'role' là trường lưu thông tin quyền
        //     alert("Bạn không có quyền truy cập trang hoặc thực hiện chức năng này.");
        //     localStorage.removeItem('jwt');  

        //     window.location.href = "401-author-page.html"; // Chuyển đến trang thông báo không có quyền

        //     localStorage.setItem('redirectUrl', window.location.href); 
        // }

 
        //  if (jwtJson.role === "ROLE_HOTEL_MANAGER" && window.location.href.includes("guest")) {
        //     localStorage.removeItem('jwt');  
        //     alert("Hotel Manager không có quyền truy cập trang này.");
        //     window.location.href = "401-author-page.html"; // Chuyển đến trang thông báo không có quyền
        // }
    }
});

function handleLogout() {
    let token = localStorage.getItem('jwt');

    if (token) {
        $.ajax({
            url: '/authen/logout',
            type: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            success: function(response) {
                // Xóa token khỏi localStorage
                localStorage.removeItem('jwt');
                alert('Logged Out!');
                // Điều hướng đến trang đăng nhập hoặc trang chủ
                window.location.href = 'signin.html';
            },
            // error: function(xhr, status, error) {
            //     console.error('Error logging out:', error);
            //     alert('Failed to log out. Please try again.');
            // }
        });
    } else {
        alert('No token found.');
        window.location.href = 'signin.html';
    }
}

function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}


// Kiểm tra token có hết hạn hay chưa
function isTokenExpired(token) {
    var jwtJson = parseJwt(token);
    var currentTime = Math.floor(Date.now() / 1000); // Lấy thời gian hiện tại
    return jwtJson.exp && jwtJson.exp < currentTime; // So sánh thời gian hết hạn với hiện tại
}

function setItemWithExpiry(key, value, ttl) {
    const now = new Date();
    
    // Tạo một đối tượng để lưu giá trị và thời gian hết hạn
    const item = {
        value: value,
        expiry: now.getTime() + ttl // ttl là thời gian sống của giá trị (tính bằng milliseconds)
    };

    localStorage.setItem(key, JSON.stringify(item)); // Lưu đối tượng dưới dạng chuỗi JSON
}
