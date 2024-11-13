$(document).ready(function () {
    $('#signup').click(function (e) {
        e.preventDefault(); // Ngăn chặn hành động submit mặc định

        // Tạo đối tượng FormData để lưu trữ dữ liệu form
        var formData = new FormData();
        formData.append('firstname', $('#firstname').val());
        formData.append('lastname', $('#lastname').val());
        formData.append('username', $('#username').val());
        formData.append('email', $('#email').val());
        formData.append('phone', $('#PhoneNumber').val());
        formData.append('password', $('#password').val());

        // Kiểm tra dữ liệu
        var isValid = true;
        var message = "";

        // Kiểm tra số điện thoại (chỉ chứa số)
        var phoneRegex = /^\d+$/;
        if (!$('#PhoneNumber').val().match(phoneRegex)) {
            message += "Số điện thoại chỉ được chứa số.\n";
            isValid = false;
        }

        // Kiểm tra họ và tên (không chứa số hoặc ký tự đặc biệt)
        var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
        if (!$('#firstname').val().match(nameRegex) || !$('#lastname').val().match(nameRegex)) {
            message += "Họ tên không được chứa số hoặc ký tự đặc biệt.\n";
            isValid = false;
        }

        // Kiểm tra định dạng email
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!$('#email').val().match(emailRegex)) {
            message += "Email chưa đúng định dạng.\n";
            isValid = false;
        }

        // Nếu không hợp lệ, hiển thị thông báo lỗi
        if (!isValid) {
            alert(message);
        } else {
            // Gửi yêu cầu AJAX
            $.ajax({
                url: 'http://localhost:9999/signup', // Thay bằng URL API thực tế
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (response) {
                    if (response.status === 200 || response.statusCode === 200) {
                        alert('Registered successfully! Please check your email to confirm your account');
                    } else {
                        alert('Registration failed: ' + (response.message || 'Lỗi không xác định.'));
                    }
                },
                error: function (xhr, status, error) {
                    let errorMessage = xhr.responseJSON ? xhr.responseJSON.message : error;
                    alert('Lỗi: ' + errorMessage);
                    console.error('Chi tiết lỗi:', errorMessage);
                }
            });
        }
    });
});
