$(document).ready(function () {
    $('#send').click(function (e) {
        e.preventDefault(); // Ngăn chặn hành động submit mặc định

        // Lấy email từ trường input
        var email = $('#email').val();

        // Kiểm tra định dạng email
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email.match(emailRegex)) {
            alert("Email chưa đúng định dạng.");
            return;
        }

        // Gửi yêu cầu AJAX
        $.ajax({
            url: 'http://localhost:9999/email/resend', // Đường dẫn API để gửi email
            type: 'GET',
            data: { email: email }, // Thêm email vào query parameters
            success: function (response) {
                if (response.message) {
                    alert('Verification email has been sent: ' + response.message);
                } else {
                    alert('Không thể gửi email: Lỗi không xác định.');
                }
            },
            error: function (xhr, status, error) {
                let errorMessage = xhr.responseJSON ? xhr.responseJSON.message : error;
                alert('Lỗi: ' + errorMessage);
                console.error('Chi tiết lỗi:', errorMessage);
            }
        });
    });
});
