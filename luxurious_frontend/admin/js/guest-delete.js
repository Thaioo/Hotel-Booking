$(document).ready(function () {
    var token = localStorage.getItem('jwt');
    
    $(document).on('click', '.delete-btn',function(e) {
        e.preventDefault();

        // Lấy id từ href
        var url = $(this).attr('href'); // Lấy url href="#?id={item.id}" từ code
        var id = new URLSearchParams(url.split('?')[1]).get('id'); // Lấy id từ URL

        if (!id) {
            alert('Không tìm thấy ID của khách cần xóa.');
            return;
        }        
        
        // Hiển thị thông báo xác nhận
        var isConfirmed = confirm("Confirm");

        if (isConfirmed) {
            // Nếu người dùng xác nhận xóa
            $.ajax({
                url: "http://localhost:9999/user/deleteguest",
                method: "POST",
                contentType: "application/x-www-form-urlencoded", // RequestParam
                headers: {
                    "Authorization": "Bearer " + token   
                },
                data: { idGuest: id },
                success: function (response) {
                    if (response.data === true) {
                        alert("Khách đã được xóa thành công.");
                        $(e.target).closest('tr').remove(); // Xóa dòng bảng hiện tại nếu cần
                    } else {
                        alert("Có lỗi xảy ra, vui lòng thử lại.");
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("Error deleting guest: " + textStatus + ": " + errorThrown);
                    alert("Xóa khách không thành công.");
                }
            });
        } else {
            // Nếu người dùng nhấn "Cancel"
            alert("Đã hủy xóa khách.");
        }
        
    });
});