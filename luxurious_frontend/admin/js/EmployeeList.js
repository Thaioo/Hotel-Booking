var token = localStorage.getItem('jwt');

$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:9999/role/allrole',  // URL API
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function(response) {
            if (response.statusCode === 200) {
                let data = response.data;
                let roleContainer = $('#list-employee');  // Chọn container để hiển thị
                roleContainer.empty();  // Xóa các phần tử cũ nếu có

                // Lặp qua danh sách role và nhân viên tương ứng
                $.each(data, function(index, roleData) {
                    let roleCard = `
                        <div class="col-xxl-4 col-xl-4 col-md-6">
                            <div class="role-card">
                                <div class="lh-card lh-role">
                                    <div class="lh-card-header">
                                        <h4 class="lh-card-title">${roleData.name}</h4>
                                    </div>
                                    <div class="lh-card-content label-card">`;

                    // Lặp qua danh sách nhân viên trong từng role
                    $.each(roleData.employees, function(index, employee) {
                        roleCard += `
                            <div class="lh-role-item d-flex justify-content-between align-items-center">
                                <div class="team-detail d-flex align-items-center">
                                    <div class="employee-image">
                                        <img src="${employee.image}" alt="Employee Image" class="img-fluid rounded-circle" style="width: 50px; height: 50px; object-fit: cover;">
                                    </div>
                                    <div class="info">
                                        <h6 class="mb-0">${employee.firstname} ${employee.lastname}</h6>
                                        <p class="mb-0">${employee.email}</p>
                                    </div>
                                </div>
                                <div class="dropdown">
                                    <button class="btn btn-link text-dark p-0" type="button" id="dropdownMenuButton-${employee.id}" data-bs-toggle="dropdown" aria-expanded="false" style="font-size: 24px;">
                                        <i class="mdi mdi-dots-vertical"></i> <!-- Ba chấm -->
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton-${employee.id}">
                                        <li><button class="dropdown-item text-primary view-btn" onclick="viewEmployee(${employee.id})">View</button></li>
                                        <li><button class="dropdown-item text-warning edit-btn" onclick="UpdateEmployee(${employee.id})">Update</button></li>
                                        <li><button class="dropdown-item text-danger delete-btn" data-id="${employee.id}">Delete</button></li>
                                    </ul>
                                </div>
                            </div>`;
                    });

                    // Kết thúc thẻ card
                    roleCard += `
                                    </div>
                                </div>
                            </div>
                        </div>`;

                    roleContainer.append(roleCard);  // Thêm thẻ card vào container
                });
            } else {
                console.error('Failed to fetch roles and employees');
            }
        },
        error: function(error) {
            console.error('Error:', error);
        }
    });
});

// Hàm để chuyển hướng đến trang team-profile.html với employeeId
function viewEmployee(employeeId) {
    window.location.href = `team-profile.html?id=${employeeId}`;
}

function UpdateEmployee(employeeId) {
    window.location.href = `team-update.html?id=${employeeId}`;
}