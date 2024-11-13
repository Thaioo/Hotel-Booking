$(document).ready(function () {
    var token = localStorage.getItem('jwt');

    loadGuestData();

    $('#guest-update-btn').click(function(event){

        // Ngăn hành vi mặc định của form khi click vào button tránh mất giá trị vừa nhập khi click mà giá trị sai
        event.preventDefault();

        var idGuest = $('#idguest').val();
        if (!idGuest) {
            alert("Không tìm thấy ID của khách.");
            return;
        }

        // var idGuest = $('#idguest').val()
        var fullName = $('#fullname').val()
        var dob = $('#dob').val()
        var phone = $('#phone').val()
        var email = $('#email').val().toLowerCase()
        var address = $('#address').val()
        var summary = $('#summary').val()

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
        var dobRegex = /^\d{4}-\d{2}-\d{2}$/;
        if (!dob.match(dobRegex)) {
            message += "Định dạng ngày sinh phải là yyyy-mm-dd.\n";
            isValid = false;
        }

        // Kiểm tra định dạng email
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email.match(emailRegex)) {
            message += "Email chưa đúng định dạng.\n";
            isValid = false;
        }

        if (!isValid) {
            alert(message);  // Hiện thông báo lỗi
        } else {
            // Tạo một đối tượng chứa các thông tin từ form (theo đúng cấu trúc của AddGuestRequest)
            var guestData = {
                idGuest: idGuest,
                fullName: fullName,
                dob: dob,
                phone: phone,
                email: email,
                address: address,
                summary: summary
            };
            console.log(JSON.stringify(guestData));
            
            $.ajax({
                url: "http://localhost:9999/user/updateguest",
                method: "POST",
                contentType:"application/json", // RequestBody
                headers: {
                    "Authorization": "Bearer " + token
                },
                data:JSON.stringify({
                    
                        "idGuest": guestData.idGuest,
                        "fullName": guestData.fullName,
                        "phone": guestData.phone,
                        "email": guestData.email,
                        "dob": guestData.dob,
                        "address": guestData.address, 
                        "summary": guestData.summary
                    
                })
            }).done(function(item){
                if (item.statusCode === 200 && item.data === true) {
                    alert("Update thành công!");
                    window.location.href= 'guest.html';
                }else if (item.statusCode === 500 && item.message === "Duplicate mail or phone number"){
                    alert("Phone hoặc mail đã tồn tại, vui lòng kiểm tra hoặc nhập lại.");
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

// Function to load guest data
function loadGuestData() {
    var searchParam = new URLSearchParams(window.location.search); // quét tìm id
    var id = searchParam.get("id"); // hứng id vào biến sau khi quét

    if (!id) {
        alert('Không tìm thấy ID của khách.');
    }

    $.ajax({
        url: `http://localhost:9999/user/getvalueuserbyid/${id}`, // PathVariable
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token
        },
    }).done(function(item){
        
        if(item.data.id > 0){
            var valueGuest = item.data;
            $('#idguest').val(valueGuest.id);
            $('#fullname').val(valueGuest.fullName);  // gán giá trị trong các ô input
            $('#dob').val(valueGuest.dob);
            $('#phone').val(valueGuest.phone);
            $('#email').val(valueGuest.email);
            $('#address').val(valueGuest.address);
            $('#summary').val(valueGuest.summary);
        }else{
            alert('Không thể tải dữ liệu người dùng.');
        }
        
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log("Error checking value guest: " + textStatus + ": " + errorThrown);
    });
}