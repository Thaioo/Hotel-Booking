// $(document).ready(function() {
//      $.ajax({
//         url: "http://localhost:9999/room/allRoom",  
//         type: "GET",
//         dataType: "json",
//         contentType: "application/json",
//         success: function(response) {
//              $("#list-rooms").empty();

//              if (response && response.data && response.data.length > 0) {
//                  response.data.forEach(function(room) {
//                     var row = $("<tr></tr>");  

//                      row.append("<td>" + room.id + "</td>");
//                     row.append("<td>" + room.name + "</td>");
//                     row.append("<td>" + room.roomTypeDTO.name + "</td>");
//                     row.append('<td class="text-right">' +
//                         '<button class="btn btn-primary btn-sm mr-2 edit-room" data-id="' + room.id + '">Edit</button>' +
//                         '<button class="btn btn-danger btn-sm delete-room" data-id="' + room.id + '">Delete</button>' +
//                         '</td>');

//                      $("#list-rooms").append(row);
//                 });

//                  $(".delete-room").click(function() {
//                      var roomId = $(this).data("id");

//                      if (confirm("Are you sure you want to delete this room?")) {
//                          $.ajax({
//                             url: "http://localhost:9999/room/" + roomId,  
//                             type: "DELETE",
//                             success: function(response) {
//                                 if(response.statusCode===200){
//                                     alert("Room deleted successfully!");

//                                     $('button[data-id="' + roomId + '"]').closest('tr').remove();
//                                 }
                                
//                             },
//                             error: function(error) {
//                                 console.error("Error deleting room:", error);
//                                 alert("Failed to delete the room.");
//                             }
//                         });
//                     }
//                 });

//             } else {
//                  var emptyRow = $("<tr></tr>");
//                 emptyRow.append("<td colspan='4' class='text-center'>No rooms available</td>");
//                 $("#list-rooms").append(emptyRow);
//             }
//         },
//         error: function(error) {
//             console.error("Error fetching data:", error);
//              var errorRow = $("<tr></tr>");
//             errorRow.append("<td colspan='4' class='text-center text-danger'>Error loading rooms</td>");
//             $("#list-rooms").append(errorRow);
//         }
//     });
// });

var token = localStorage.getItem('jwt');

$(document).ready(function() {

    loadRoomTypes();

    // Hàm để tải loại phòng
    async function loadRoomTypes() {
        try {
            const response = await $.ajax({
                url: "http://localhost:9999/roomType",
                type: "GET",
                //dataType: "json",
                contentType: "application/json",
                headers: {
                    "Authorization": "Bearer " + token   
                }
            });

            // Kiểm tra xem dữ liệu có hợp lệ không
            if (response && response.data && response.data.length > 0) {
                // Làm đầy dropdown loại phòng
                response.data.forEach(function(roomType) {
                    $('#editRoomType').append(new Option(roomType.name, roomType.id)); // Giả định roomType có fields 'name' và 'id'
                });
            } else {
                // Nếu không có loại phòng nào, bạn có thể xử lý ở đây
                $('#editRoomType').append('<option disabled selected>No room types available</option>');
            }
        } catch (error) {
            console.error("Error fetching room types:", error);
            $('#editRoomType').append('<option disabled selected>Error loading room types</option>');
        }
    }
    $.ajax({
        url: "http://localhost:9999/room/allRoom",  
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token   
        },
        success: function(response) {
            $("#list-rooms").empty();

            if (response && response.data && response.data.length > 0) {
                response.data.forEach(function(room) {
                    var row = $("<tr></tr>");  
                    row.append("<td>" + room.id + "</td>");
                    row.append("<td>" + room.name + "</td>");
                    row.append("<td>" + room.roomTypeDTO.name + "</td>");
                    row.append('<td class="text-right">' +
                        '<button class="btn btn-primary btn-sm mr-2 edit-room" data-id="' + room.id + '">Edit</button>' + // Thêm class edit-room
                        '<button class="btn btn-danger btn-sm delete-room" data-id="' + room.id + '">Delete</button>' +
                        '</td>');

                    $("#list-rooms").append(row);
                });

                // Sự kiện click cho nút Delete
                $(".delete-room").click(function() {
                    var roomId = $(this).data("id");

                    if (confirm("Are you sure you want to delete this room?")) {
                        $.ajax({
                            url: "http://localhost:9999/room/" + roomId,  
                            type: "DELETE",
                            headers: {
                                "Authorization": "Bearer " + token   
                            },
                            success: function(response) {
                                if (response.statusCode === 200) {
                                    alert("Room deleted successfully!");
                                    $('button[data-id="' + roomId + '"]').closest('tr').remove();
                                }
                            },
                            error: function(error) {
                                console.error("Error deleting room:", error);
                                alert("Failed to delete the room.");
                            }
                        });
                    }
                });

                // Sự kiện click cho nút Edit
                $(".edit-room").click(function() {
                    var roomId = $(this).data("id");

                    // Gọi API để lấy thông tin phòng
                    $.ajax({
                        url: "http://localhost:9999/room/getRoom/" + roomId,  
                        type: "GET",
                        dataType: "json",
                        headers: {
                            "Authorization": "Bearer " + token   
                        },
                        success: function(response) {
                            if (response && response.data) {
                                // Điền thông tin vào form
                                $('#editRoomId').val(response.data.id);
                                $('#editRoomName').val(response.data.name);
                                $('#editRoomType').val(response.data.roomTypeDTO.id); // Giả định roomTypeDTO có field id
                                
                                // Hiện modal
                                $('#editRoomModal').modal('show');
                            }
                        },
                        error: function(error) {
                            console.error("Error fetching room details:", error);
                            alert("Failed to load room details for editing.");
                        }
                    });
                });

            } else {
                var emptyRow = $("<tr></tr>");
                emptyRow.append("<td colspan='4' class='text-center'>No rooms available</td>");
                $("#list-rooms").append(emptyRow);
            }
        },
        error: function(error) {
            console.error("Error fetching data:", error);
            var errorRow = $("<tr></tr>");
            errorRow.append("<td colspan='4' class='text-center text-danger'>Error loading rooms</td>");
            $("#list-rooms").append(errorRow);
        }
    });
});


$(document).ready(function() {
    // Gọi API để lấy loại phòng khi trang được load
    loadRoomTypes();

    // Hàm để tải loại phòng
    async function loadRoomTypes() {
        try {
            const response = await $.ajax({
                url: "http://localhost:9999/roomType",
                type: "GET",
                dataType: "json",
                headers: {
                    "Authorization": "Bearer " + token   
                }
            });

            // Kiểm tra xem dữ liệu có hợp lệ không
            if (response && response.data && response.data.length > 0) {
                // Làm đầy dropdown loại phòng
                response.data.forEach(function(roomType) {
                    $('#roomType').append(new Option(roomType.name, roomType.id)); // Giả định roomType có fields 'name' và 'id'
                });
            } else {
                // Nếu không có loại phòng nào, bạn có thể xử lý ở đây
                $('#roomType').append('<option disabled selected>No room types available</option>');
            }
        } catch (error) {
            console.error("Error fetching room types:", error);
            $('#roomType').append('<option disabled selected>Error loading room types</option>');
        }
    }

    // Mã AJAX để thêm phòng
    $('#addRoomForm').on('submit', async function(event) {
        event.preventDefault(); // Ngăn chặn submit mặc định

        // Lấy dữ liệu từ form
        const roomData = {
            name: $('#roomName').val(),
            idRoomType: parseInt($('#roomType').val()) // Chuyển đổi sang int
        };

        console.log(roomData);

        try {
            // Gọi API để thêm phòng
            const response = await $.ajax({
                url: 'http://localhost:9999/room/addRoom', // Đường dẫn đến API thêm phòng
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(roomData),
                headers: {
                    "Authorization": "Bearer " + token   
                }
            });

            if (response.statusCode === 200) {
                alert("Room added successfully!");
                // Có thể gọi lại loadRoomTypes() hoặc cập nhật danh sách phòng ở đây
                location.reload();
            } 
            
            
            else {
                alert('Error: ' + response.message); // Hiển thị thông điệp lỗi từ API nếu có
            }
        } catch (error) {
            alert('Error adding room: ' + error);
        }
    });
});




$(document).ready(function() {
    // Khi nhấn nút "Save Changes"
    $("#editRoomForm").on("submit", function(event) {
        event.preventDefault(); // Ngăn chặn việc gửi form theo cách mặc định

        // Lấy dữ liệu từ form
        var roomId = $("#editRoomId").val(); // Lấy ID phòng
        var roomName = $("#editRoomName").val(); // Lấy tên phòng
        var roomTypeId = $("#editRoomType").val(); // Lấy ID loại phòng đã chọn

        // Tạo đối tượng yêu cầu cập nhật
        var updateRoomRequest = {
            id: roomId,
            name: roomName,
            idRoomType: roomTypeId
        };

        console.log(updateRoomRequest);



        // Gửi yêu cầu PUT đến API
        $.ajax({
            url: "http://localhost:9999/room", // Địa chỉ API
            type: "PUT", // Phương thức PUT
            contentType: "application/json", // Loại nội dung
            dataType: "json", // Kiểu dữ liệu trả về
            data: JSON.stringify(updateRoomRequest), // Chuyển đổi đối tượng thành JSON
            headers: {
                "Authorization": "Bearer " + token   
            },
            success: function(response) {
                // Xử lý kết quả trả về từ API
                if (response && response.data) {
                     
                    alert(response.data);  
                    location.reload();
                     
                } else {
                    alert("Update failed.");
                }
            },
            error: function(error) {
                console.error("Error updating room:", error);
                alert("Failed to update room.");
            }
        });
    });
});