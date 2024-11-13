$(document).ready(function () {
    var token = localStorage.getItem('jwt');

    $('#guestadd-btn').click(async function(){

        // Ngăn hành vi mặc định của form khi click vào button tránh mất giá trị vừa nhập khi click mà giá trị sai
        event.preventDefault();

        var fullName = $('#fullname').val()
        var dob = $('#dob').val()
        var phone = $('#phone').val()
        var email = $('#email').val().toLowerCase()
        var address = $('#address').val()
        var summary = $('#summary').val()
        var imageUpload = $('#imageUpload')[0].files[0]; // Lấy file từ input

        // Kiểm tra dữ liệu
        var isValid = true;
        var message = "";

        // Kiểm tra fullname (không chứa số hoặc ký tự đặc biệt)
        var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
        if (!fullName.match(nameRegex)) {
            message += "Họ tên không được để trống, chứa số hoặc ký tự đặc biệt.\n";
            isValid = false;
        }

        // Kiểm tra phone (chỉ chứa số)
        var phoneRegex = /^\d+$/;
        if (!phone.match(phoneRegex)) {
            message += "Số điện thoại chỉ được chứa số.\n";
            isValid = false;
        }

        // Kiểm tra định dạng dob (yyyy-mm-dd)
        // var dobRegex = /^\d{4}-\d{2}-\d{2}$/;
        // if (!dob.match(dobRegex)) {
        //     message += "Định dạng ngày sinh phải là yyyy-mm-dd.\n";
        //     isValid = false;
        // }

        // Kiểm tra định dạng email
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email.match(emailRegex)) {
            message += "Email chưa đúng định dạng.\n";
            isValid = false;
        }

        
        if (!isValid) {
            alert(message);  // Hiện thông báo lỗi
        } else {
            // Tạo một đối tượng FormData để gửi request
            var formData = new FormData();
            formData.append("fullName", fullName);
            formData.append("dob", dob);
            formData.append("phone", phone);
            formData.append("email", email);
            formData.append("address", address);
            formData.append("summary", summary);
            formData.append("filePicture", imageUpload);  // Đính kèm file vào formData
            
            $.ajax({
                url: "http://localhost:9999/user/addguest",
                headers: {
                    "Authorization": "Bearer " + token  // Truyền token vào header
                },
                method: "POST",
                data: formData,
                processData: false, // Ngăn chặn jQuery xử lý dữ liệu (vì chúng ta đang sử dụng FormData)
                contentType: false, // Để mặc định, vì chúng ta đang gửi multipart/form-data
            }).done(function(item){
                if (item.statusCode === 200) {
                    alert("Thêm khách thành công!");
                    // Bạn có thể reset form hoặc thực hiện hành động khác
                    $('#fullname').val('');  // Xóa giá trị trong các ô input
                    $('#dob').val('');
                    $('#phone').val('');
                    $('#email').val('');
                    $('#address').val('');
                    $('#summary').val('');
                }else if (item.statusCode === 500 && item.message === "duplicate mail or phone number" ) {
                    alert("Số điện thoại hoặc email đã tồn tại.\nMời bạn nhập lại.");
                } else {
                    alert("Đã có lỗi xảy ra, vui lòng thử lại.");
                }
        
            }).fail(function(jqXHR, textStatus, errorThrown) {
                alert("Có lỗi xảy ra: " + textStatus + ": " + errorThrown);
            });
        }

        

    });

    // Đoạn mã tự động thêm dấu gạch ngang vào ngày sinh
    document.getElementById('dob').addEventListener('input', function(e) {
        // Lấy giá trị hiện tại trong trường input
        let input = e.target.value.replace(/\D/g, '');  // Thay đổi này giúp loại bỏ ký tự không phải số

        // Xóa tất cả các dấu gạch ngang nếu có
        input = input.replace(/-/g, '');

        // Kiểm tra nếu độ dài chuỗi đủ để chèn dấu gạch ngang
        if (input.length > 4 && input.length <= 6) {
            // Chèn dấu gạch ngang giữa năm và tháng
            input = input.slice(0, 4) + '-' + input.slice(4);
        } else if (input.length > 6) {
            // Chèn dấu gạch ngang giữa năm, tháng và ngày
            input = input.slice(0, 4) + '-' + input.slice(4, 6) + '-' + input.slice(6);
        }

        // Cập nhật giá trị của trường input với dấu gạch ngang
        e.target.value = input;
        
    });   

});