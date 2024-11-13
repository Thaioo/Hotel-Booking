$(document).ready(function() {
    var token = localStorage.getItem('jwt');
     $(document).on('click', '.delete-btn', function() {
        let employeeId = $(this).data('id');
        if (confirm('Bạn có chắc muốn xóa nhân viên ID: ' + employeeId + '?')) {
            $.ajax({
                url: 'http://localhost:9999/employee/' + employeeId,   
                method: 'DELETE',
                headers: {
                    "Authorization": "Bearer " + token   
                },
                success: function(response) {
                    if (response.statusCode === 200) {
                        alert('Xóa nhân viên thành công');
                        location.reload();   
                    } else {
                        alert('Xóa nhân viên thất bại');
                    }
                },
                error: function(error) {
                    console.error('Error:', error);
                    alert('Xảy ra lỗi khi xóa nhân viên');
                }
            });
        }
    });
});
