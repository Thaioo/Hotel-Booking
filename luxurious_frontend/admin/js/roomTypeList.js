// var token = localStorage.getItem('jwt');
// $(document).ready( function(){
    
//     $.ajax({
//         url: "http://localhost:9999/roomType",
//         method: "GET",
//         contentType: "application/json",
//         headers: {
//             "Authorization": "Bearer " + token  // Truyền token vào header
//         }
//     }).done(function( response ) {

//         if(response.statusCode===401){
//             alert('khong dc phan quyen')
//             window.location.href = "401-author-page.html"

//         }
//           if(response.data){
//             var html=""
            
//             for(i=0;i<response.data.length;i++){
//                 var item = response.data[i];
//                     var ruoomNum=""

//                 if(item.roomName && item.roomName.length >0){
//                     for(j=0; j< item.roomName.length;j++){
//                         ruoomNum += item.roomName[j];
//                         if(j<item.roomName.length-1){
//                             ruoomNum += ", "
//                         }
//                     }
//                 }else{
//                     ruoomNum="No Rooms Available"
//                 }

//                 html+= `													<tr>
// 														<td class="token">${item.name}</td>
// 														<td><img class="cat-thumb" src="${item.image[0]}"
// 																alt="clients Image"><span class="name">${item.overview}</span>
// 														</td>
// 														<td>$${item.price}</td>
// 														<td>${item.area}m2</td>
// 														<td>${item.capacity}</td>
// 														<td>${item.bedName}</td>
//                                                         <td>${ruoomNum}</td>
														
														
														
// 														<td>
// 															<div class="d-flex justify-content-center">
// 																<!-- <button type="button" class="btn btn-outline-success"><i
// 																		class="ri-information-line"></i></button> -->
// 																<button type="button"
// 																	class="btn btn-outline-success dropdown-toggle dropdown-toggle-split"
// 																	data-bs-toggle="dropdown" aria-haspopup="true"
// 																	aria-expanded="false" data-display="static">
// 																	<span class="sr-only"><i
// 																			class="ri-settings-3-line"></i></span>
// 																</button>
// 																<div class="dropdown-menu">
// 																	<span class="dropdown-item edit-btn" href="#" data-id="${item.id}">Edit</span>
// 																	<span class="dropdown-item delete-btn" href="#" data-id="${item.id}">Delete</span>
// 																</div>
// 															</div>
// 														</td>
// 													</tr>`
//             }
//             $('#list-roomtype').append(html)
//           }
//     });
// })







var token = localStorage.getItem('jwt');

$(document).ready(function () {
    $.ajax({
        url: "http://localhost:9999/roomType",
        method: "GET",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token   
        }
    }).done(function (response) {
         if (response.data) {
            var html = "";

            for (i = 0; i < response.data.length; i++) {
                var item = response.data[i];
                var ruoomNum = "";

                if (item.roomName && item.roomName.length > 0) {
                    for (j = 0; j < item.roomName.length; j++) {
                        ruoomNum += item.roomName[j];
                        if (j < item.roomName.length - 1) {
                            ruoomNum += ", ";
                        }
                    }
                } else {
                    ruoomNum = "No Rooms Available";
                }

                html += `<tr>
                            <td class="token">${item.name}</td>
                            <td><img class="cat-thumb" src="${item.image[0]}" alt="clients Image">
                                <span class="name">${item.overview}</span>
                            </td>
                            <td>$${item.price}</td>
                            <td>${item.area}m2</td>
                            <td>${item.capacity}</td>
                            <td>${item.bedName}</td>
                            <td>${ruoomNum}</td>
                            <td>
                                <div class="d-flex justify-content-center">
                                    <button type="button"
                                        class="btn btn-outline-success dropdown-toggle dropdown-toggle-split"
                                        data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                                        data-display="static">
                                        <span class="sr-only"><i class="ri-settings-3-line"></i></span>
                                    </button>
                                    <div class="dropdown-menu">
                                        <span class="dropdown-item edit-btn" href="#" data-id="${item.id}">Edit</span>
                                        <span class="dropdown-item delete-btn" href="#" data-id="${item.id}">Delete</span>
                                        <a class="dropdown-item" href="room-list.html">Add Room</a>
                                    </div>
                                </div>
                            </td>
                        </tr>`;
            }
            $('#list-roomtype').append(html);
        }
    }).fail(function (jqXHR) {
        // Xử lý lỗi khi mã trạng thái là 401 (Unauthorized)
        if (jqXHR.statusCode === 401) {
            //alert('Bạn không có quyền truy cập vào trang này.');
            // window.location.href = "401-author-page.html";  
            // localStorage.setItem('redirectUrl', window.location.href); 
        } else {
            // window.location.href = "401-author-page.html";      
            // localStorage.setItem('redirectUrl', window.location.href);   
            }
    });
});


//<td>${item.amenity}</td>




$(document).ready(function() {
    
    $('#list-roomtype').on('click', '.dropdown-item.delete-btn', function(event) {
        //event.preventDefault(); 

        var row = $(this).closest('tr'); // Lấy hàng chứa nút được nhấn
        var roomId = $(this).data('id'); // Lấy ID từ thuộc tính data-id

       
        if(confirm('Are you sure you want to delete this RoomType data?')) {
            $.ajax({
                url: "http://localhost:9999/roomType/" + roomId, 
                method: "DELETE",
                contentType: "application/json",
                headers: {
                    "Authorization": "Bearer " + token   
                },
                success: function(response) {
                    if(response.statusCode == 200) {
                        
                        row.remove(); 
                        alert('Xóa thành công');
                    } else {
                        alert('Xóa thất bại: ' + response.message);
                    }
                },
                error: function() {
                    // Function thong báo, nếu xoá không được
                     showCustomAlert();
                }
            });
        }
    });
});




 
// hiển thị hình ảnh tạm

$(document).ready(function() {
    $('#imageUpload').on('change', function() {
        $('#imagePreview').empty();  
        var files = this.files;

        if (files) {
            $.each(files, function(index, file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var img = $('<img>').attr('src', e.target.result).attr('class', 'img-fluid').css('margin-right', '4px').css('margin-bottom', '4px');
                    $('#imagePreview').append(img);
                }
                reader.readAsDataURL(file);
            });
        }
    });
});

// edit front end


$(document).ready(function() {
    
    $(document).on('click', '.edit-btn', function(event) {
        event.preventDefault();  
        var roomId = $(this).data('id');  
        localStorage.setItem('editRoomId', roomId);
        window.location.href = "RoomTypeUpdate.html";
    });
});


$(document).ready(function() {
     
    var imagesToDelete = [];
    var existingImages = [];  
     var roomId = localStorage.getItem('editRoomId');

    if (roomId) {
        $.ajax({
            url: "http://localhost:9999/roomType/detail/" + roomId,
            method: "GET",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token   
            },
            success: function(response) {
                if (response.statusCode === 200) {
                    var room = response.data;

                     
                    $('input[name="id"]').val(room.id);
                    $('input[name="name"]').val(room.name);
                    $('input[name="overview"]').val(room.overview);
                    $('input[name="price"]').val(room.price);
                    $('input[name="area"]').val(room.area);
                    $('input[name="capacity"]').val(room.capacity);
                    $('select[name="iDBedType"]').val(room.bedNum);

                    
                    if (room.image && room.image.length > 0) {
                        $('#imagePreview').empty(); 
                        room.image.forEach(function(imageUrl, index) {
                            var imgContainer = $('<div>').addClass('image-container').css({
                                'display': 'inline-block',
                                'position': 'relative',
                                'margin-right': '10px',
                                'margin-bottom': '10px'
                            });

                            var img = $('<img>').attr('src', imageUrl)
                                                .attr('class', 'img-fluid')
                                                .css('max-width', '100px'); 
                            
                            var removeButton = $('<button>').text('X')
                                                .attr('data-url', imageUrl)  
                                                .addClass('remove-image-btn')
                                                .css({
                                                    'position': 'absolute',
                                                    'top': '0',
                                                    'right': '0',
                                                    'background-color': 'red',
                                                    'color': 'white',
                                                    'border': 'none',
                                                    'cursor': 'pointer'
                                                });

                            imgContainer.append(img).append(removeButton);
                            $('#imagePreview').append(imgContainer);
                            
                            
                            existingImages.push(imageUrl);
                        });
                    }
 
                    if (room.amenity) {
                        const amenityMap = {
                            "Free Wi-Fi": 1,
                            "Swimming Pool": 2,
                            "Fitness Center": 3,
                            "24-Hour Reception": 4,
                            "Room Service": 5,
                            "Laundry Service": 6,
                            "Parking": 7,
                            "Air Conditioning": 8,
                            "Breakfast Included": 9,
                            "Airport Shuttle": 10,
                            "Spa and Wellness Center": 11,
                            "Pet-Friendly": 12,
                            "Non-Smoking Rooms": 13,
                            "Conference Facilities": 14,
                            "Restaurant": 15,
                            "Bar/Lounge": 16,
                            "Business Center": 17,
                            "Cable/Satellite TV": 18,
                            "Mini-Bar": 19,
                            "Balcony/Terrace": 20
                        };

                        var amenityArray = room.amenity.split(',').map(function(item) {
                            return item.trim();
                        });

                        amenityArray.forEach(function(amenity) {
                            var amenityId = amenityMap[amenity];
                            if (amenityId) {
                                $('input[name="idAmenity"][value="' + amenityId + '"]').prop('checked', true);
                            }
                        });
                    }

                } else {
                    alert('Failed to load room type details: ' + response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error("Status: " + status);
                console.error("Error: " + error);
                console.error("Response: " + xhr.responseText);
                
            }
        });
    } else {
     }

     
    $(document).on('click', '.remove-image-btn', function() {
        var imageUrl = $(this).data('url');  
        imagesToDelete.push(imageUrl);  

        $(this).parent().remove();  

         
        existingImages = existingImages.filter(function(img) {
            return img !== imageUrl;
        });
    });

     
    $('#updateRoomTypeForm').on('submit', function(event) {
        event.preventDefault();  

 
        var formData = new FormData(this);

         
        formData.append('imagesToDelete', JSON.stringify(imagesToDelete));

         
        formData.append('existingImages', JSON.stringify(existingImages));

         
        var newImages = $('#imageUpload')[0].files;
        for (var i = 0; i < newImages.length; i++) {
            formData.append('newImages[]', newImages[i]);
        }


          
        $.ajax({
            url: "http://localhost:9999/roomType",  
            method: "PUT",
            data: formData,
            processData: false,  
            contentType: false,
            headers: {
                "Authorization": "Bearer " + token   
            },
            success: function(response) {
                if (response.statusCode === 200) {
                    alert('Room type updated successfully!');
                    localStorage.removeItem('editRoomId');
                    window.location.href = "room_type_mana.html";
                } else {
                    alert('Failed to update room type: ' + response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error("Status: " + status);
                console.error("Error: " + error);
                console.error("Response: " + xhr.responseText);
                 
            }
        });
    });
});


function showCustomAlert() {
    var alertBox = document.getElementById('customAlert');
    var alertMessage = document.getElementById('alertMessage');
    var alertLink = document.getElementById('alertLink');

     alertMessage.textContent = 'Cannot delete this RoomType, Click this link to ';
    alertLink.href = 'room-list.html';  

     alertBox.style.display = 'flex';

     document.getElementById('closeAlert').onclick = function() {
        alertBox.style.display = 'none';
    };
}

 









 
    // $(document).ready(function() {
    //     // Hàm để gọi API và lấy hình ảnh
    //     function loadImages() {
    //         $.ajax({
    //             url: 'http://localhost:9999/roomType', // URL của API
    //             method: 'GET',
    //             dataType: 'json',
    //             success: function(data) {
    //                 const imagePreview = $('#imagePreview');
    //                 imagePreview.empty(); // Xóa hình ảnh cũ trước khi tải hình ảnh mới

    //                 // Giả sử dữ liệu trả về là một mảng chứa đường dẫn hình ảnh
    //                 $.each(data, function(index, image) {
    //                     const img = $('<img>').attr('src', image).css({
    //                         width: '100px', // Thay đổi kích thước hình ảnh nếu cần
    //                         margin: '5px'   // Thêm khoảng cách giữa các hình ảnh
    //                     });
    //                     imagePreview.append(img); // Thêm hình ảnh vào div xem trước
    //                 });
    //             },
    //             error: function(xhr, status, error) {
    //                 console.error('Có lỗi xảy ra khi gọi API:', error);
    //             }
    //         });
    //     }

    //     // Gọi hàm loadImages khi trang được tải
    //     loadImages();
    // });
 