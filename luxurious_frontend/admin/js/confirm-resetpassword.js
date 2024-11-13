$(document).ready(function () { 

    // Lấy URL hiện tại
    const urlParams = new URLSearchParams(window.location.search);

    // Lấy giá trị của query parameter 'key'
    const verificationKey = urlParams.get('key');

    if (verificationKey) {
        console.log("Mã xác nhận:", verificationKey);
        // Xử lý tiếp tục với mã xác nhận này (gửi tới server để xác minh, hiển thị, v.v.)
    } else {
        console.log("Không tìm thấy mã xác nhận trong URL.");
        alert("Mã xác nhận không tồn tại.");
    }

    $('#enter-new-password').click(function (e) { 
        e.preventDefault();
        
        var password = $('#enter-password').val();
        var password_again = $('#enter-password-again').val();
        
        // Kiểm tra dữ liệu
        var isValid = true;
        var message = "";

        if (password != password_again) {
            message += "The password re-entered is incorrect";
            isValid = false;
        }

        if (!isValid) {
            alert(message);  // Hiện thông báo lỗi
        }else {
            $.ajax({
                url: "http://localhost:9999/email/changepass",
                method: "POST",
                contentType: "application/x-www-form-urlencoded", // RequestParam
                data: {
                    token: verificationKey,
                    password: password
                }
            }).done(function(item){
                if (item.statusCode === 200 && item.message === "Token expired or wrong!") {
                    alert("Token expired or wrong!");

                }else if (item.statusCode === 200 && item.message === "Complete!"){
                    alert("Reset Compele!");
                    window.location.href = "http://127.0.0.1:5501/admin/signin.html";
                }
        
            }).fail(function(jqXHR, textStatus, errorThrown) {
                alert("Có lỗi xảy ra vui lòng thử lại: " + textStatus + ": " + errorThrown);
            });
        }

    });

});