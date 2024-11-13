var token = localStorage.getItem('jwt');


$(document).ready(function () {

    // Lấy giá trị của tham số id từ URL
    var urlParams = new URLSearchParams(window.location.search);
    var id = urlParams.get('id'); // Lấy giá trị của tham số 'id'
    console.log("id nè " + id);
    

    $.ajax({
        url: "http://localhost:9999/user/allbookingguest",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
		headers: {
            "Authorization": "Bearer " + token   
        },
        data: { idGuest: id },        
    }).done(function(item){
        if(item.data && item.data.length > 0){
            var firstBooking = item.data[0]
            var valueGuest = firstBooking.guestDTO;

            var html = `<div class="col-xxl-3 col-xl-4 col-md-12">
						<div class="lh-card-sticky guest-card">
							<div class="lh-card">
								<div class="lh-card-content card-default">
									<div class="guest-profile">
										<img src="${valueGuest.linkImage}" alt="profile" style="width: 200px; height: auto;">
										<h5>${valueGuest.fullName}</h5>
										<p>${valueGuest.id}</p>
									</div>
								<!-- <ul class="list">
									<li><i class="ri-phone-line"></i><span>+91 9876543210</span></li>
									<li><i class="ri-mail-line"></i><span>support@luxurious.com</span></li>
									<li><i class="ri-map-pin-line"></i><span>47 Street view, Lorence park, Gujarat,
											Bharat.</span></li>
								</ul> -->
								</div>
							</div>
						</div>
					</div>
					<div class="col-xxl-9 col-xl-8 col-md-12">
						<div class="lh-card team-overview">
							<div class="lh-card-content lh-card-team team-details">
								<div class="row">
									<div class="col-sm-12">
										<h3>Account Details</h3>
										<div class="lh-team-detail">
											<p>${valueGuest.summary}</p>
										</div>
									</div>
									<div class="col-lg-6 col-md-12">
										<div class="lh-team-detail">
											<h6>E-mail address</h6>
											<ul>
												<li id="mail"><strong>Email : ${valueGuest.email}</strong></li>
											</ul>
										</div>
									</div>
									<div class="col-lg-6 col-md-12">
										<div class="lh-team-detail">
											<h6>Contact nubmer</h6>
											<ul>
												<li><strong>Phone Nubmer 1 : ${valueGuest.phone}</strong></li>
											</ul>
										</div>
									</div>
									
									<div class="col-lg-6 col-md-12">
										<div class="lh-team-detail">
											<h6>Address 2</h6>
											<ul>
												<li>${valueGuest.address}</li>
											</ul>
										</div>
									</div>

								</div>
							</div>
						</div>
					</div>`;

            $('#guest-details').append(html)
            var html1 = ""

			var dataTable = $('#booking_table').DataTable();
			dataTable.destroy();

            for(i=0;i<item.data.length;i++){
                
                var response = item.data[i]

                // tạo biến để lấy html của roomdetails
                var roomsDetails = '';

                // duyệt foreach để lấy danh sách roomtype kèm roomnumber tương ứng
                response.roomsDTO.forEach(function(room){
                    roomsDetails += `<span>${room.nameRoomType} : ${room.roomNumber.join(', ')}</span><br>` // thẻ br để xuống hàng sau mỗi phần tử
                })
                
				// <td><img class="cat-thumb" src="assets/img/room/1.png"
				// 	alt="clients Image"><span class="name">${response.bedType}</span>
				// </td>       
                html1 += `<tr>
													<td class="token">${response.idBooking}</td>
													
													<td>${response.checkIn}</td>
													<td>${response.checkOut}</td>
													<td>${response.paymentMethod}</td>
													<td class="active">${response.paymentStatus}</td>
													<td>${response.amount}</td>
                                                    
													<td class="type">${roomsDetails}</td>

													<td class="rooms">
														<span class="mem">${response.member} Member</span> /
														<span class="room">${response.quantilyRoom} Room</span>
													</td>
													<td>
														<div class="d-flex justify-content-center">
															<button type="button" class="btn btn-outline-success"><i
																	class="ri-information-line"></i></button>
															<button type="button"
																class="btn btn-outline-success dropdown-toggle dropdown-toggle-split"
																data-bs-toggle="dropdown" aria-haspopup="true"
																aria-expanded="false" data-display="static">
																<span class="sr-only"><i
																		class="ri-settings-3-line"></i></span>
															</button>
															<div class="dropdown-menu">
																<a class="dropdown-item" href="#">Edit</a>
																<a class="dropdown-item" href="#">Delete</a>
															</div>
														</div>
													</td>
												</tr>`

            }
            $('#guest-booking').append(html1)

			// Khởi tạo DataTable
			$('#booking_table').DataTable({
				// data: this.products, // Dữ liệu dùng để hiển thị cho bảng, có thể là array, object ...
				// columns: [
				// 	{ data: 'ID' },
				// 	{ data: 'CheckIn' },
				// 	{ data: 'slug' },
				// 	{ data: 'quantity' },
				// 	{ data: 'price' },
				// ], // Các thuộc tính của product sẽ  match với từng collumn
				// searching: false, // Mặc định là true, set false để tắt chức năng search
				// ordering:  false, // Mặc định là true, set false để tắt chức năng sắp xếp theo collumn
				// paging: false, // Mặc định là true, set false để tắt chức năng phân trang
				// scrollX: 400, // Nội dụng của table sẽ hiện thị với with 400px, Nếu quá thì sẽ có thanh scroll
				// scrollY: 400, // Nội dụng của table sẽ hiện thị với hieght 400px, Nếu quá thì sẽ có thanh scroll
				// processing: true,
				// info: false, // Tắt thông tin về table VD: Showing 1 to 14 of 14 entries	
			});
        }else{
            alert('Không thể tải dữ liệu người dùng.');
        }
        

    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log("Error checking value guestDTO: " + textStatus + ": " + errorThrown);
    });
});