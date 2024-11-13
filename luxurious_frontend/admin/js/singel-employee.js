var jwtToken = localStorage.getItem('jwt')

$(document).ready(function() {

    // Lấy employeeId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const employeeId = urlParams.get('id');
    if (employeeId) {
        // Gọi API để lấy thông tin của nhân viên
        
        $.ajax({
            url: `http://localhost:9999/employee/${employeeId}`,  // API để lấy thông tin nhân viên
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + token   
            },    
            success: function(response) {
                if (response.statusCode === 200) {
                    let employee = response.data;

                    // Hiển thị thông tin nhân viên trên trang
                    $('#employee-firstname').text(employee.firstname);
                    $('#employee-lastname').text(employee.lastname);
                    $('#employee-email').text(employee.email);
                    $('#employee-phone').text(employee.phone);
                    $('#employee-role').text(employee.role.name);
                    $('#employee-dob').text(employee.dob);  // Hiển thị ngày sinh
                    $('#employee-address').text(employee.address);  // Hiển thị địa chỉ
                    $('#employee-summary').text(employee.summary);  // Hiển thị tóm tắt

                    // Kiểm tra và hiển thị ảnh nhân viên
                    if (employee.image) {
                        $('#employee-image').attr('src', employee.image);  // Hiển thị ảnh nhân viên
                    }

                } else {
                    console.error('Failed to fetch employee details');
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    } else {
        // Nếu không có id trên URL thì hiển thị profile của user đang đăng nhập
        getMyInfo()
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

function getMyInfo() {
    $.ajax({
        url: `http://localhost:9999/employee/myInfo`,  // API để lấy thông tin nhân viên
        headers: {
            'Authorization':'Bearer ' + jwtToken,
        },
        method: 'GET',
        success: function(response) {
            if (response.statusCode === 200) {
                let employee = response.data;

                // Hiển thị thông tin nhân viên trên trang
                $('#employee-firstname').text(employee.firstname);
                $('#employee-lastname').text(employee.lastname);
                $('#employee-email').text(employee.email);
                $('#employee-phone').text(employee.phone);
                $('#employee-role').text(employee.role.name);
                $('#employee-dob').text(employee.dob);  // Hiển thị ngày sinh
                $('#employee-address').text(employee.address);  // Hiển thị địa chỉ
                $('#employee-summary').text(employee.summary);  // Hiển thị tóm tắt

                // Kiểm tra và hiển thị ảnh nhân viên
                if (employee.image) {
                    $('#employee-image').attr('src', employee.image);  // Hiển thị ảnh nhân viên
                }

            } else {
                console.error('Failed to fetch employee details');
            }
        },
        error: function(error) {
            console.error('Error:', error);
        }
    });
}