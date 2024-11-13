var token = localStorage.getItem('jwt');

$(document).ready(function () {
    var selectDate = $('#custom_single_datepicker').data('daterangepicker').startDate.format('YYYY-MM-DD');
    showAllRooms(selectDate)

    $('#custom_single_datepicker').on('apply.daterangepicker', (e, picker) => {
        selectDate = $('#custom_single_datepicker').data('daterangepicker').startDate.format('YYYY-MM-DD');
        console.log(selectDate);
        showAllRooms(selectDate)
    });
});

function showAllRooms(selectDate) {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: `http://localhost:9999/room/${selectDate}`,
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function (response) {
            $('#rooms_content').empty();
            let html = ''
            for (let i = 0; i < response.data.length; i++) {
                let roomType = response.data[i]
                html += `<div class="section-title">
					<h4>${roomType.name}</h4>
				</div>`
                html += `<div class="row">`
                for (let j = 0; j < roomType.rooms.length; j++) {
                    let room = roomType.rooms[j]
                    let guestCheckIn = {}
                    let guestCheckOut = {}
                    let guestStaying = {}

                    if (isExistObject(room.guestCheckIn)) {
                        guestCheckIn = room.guestCheckIn
                    }

                    if (isExistObject(room.guestCheckOut)) {
                        guestCheckOut = room.guestCheckOut
                    }

                    if (isExistObject(room.guestStaying)) {
                        guestStaying = room.guestStaying
                    }

                    html += `<div class="col-xl-3 col-md-6">
						<div class="lh-card booked room-card" id="bookingtbl">
							<div class="lh-card-header">
								<h4 class="lh-card-title">${room.name}</h4>
								<div class="header-tools">
									<div class="dropdown" data-bs-toggle="tooltip" data-bs-original-title="Settings">
										<button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
											<i class="mdi mdi-dots-vertical"></i>
										</button>
										<ul class="dropdown-menu">
											<li><a class="dropdown-item" href="bookings.html">Book room</a></li>
											<li><a class="dropdown-item" href="#">History</a></li>
											<li><a class="dropdown-item" href="#">Details</a></li>
										</ul>
									</div>
								</div>
							</div>
							<div class="lh-card-content card-default">
								<div class="lh-room-details">
									<ul class="list">
										<li style="color: green;">Check in: ${(guestCheckIn.lastName || '') + ' ' + (guestCheckIn.firstName || '')} - ${(guestCheckIn.phone) || ''}</li>
										<li style="color: red;">Check out: ${(guestCheckOut.lastName || '') + ' ' + (guestCheckOut.firstName || '')} - ${guestCheckOut.phone || ''}</li>
										<li style="color: blue;">Staying: ${(guestStaying.lastName || '') + ' ' + (guestStaying.firstName || '')} - ${guestStaying.phone || ''}</li>
										<li style="color: blue;">Other room: ${guestStaying.otherRooms || ''} </li>
									</ul>
								</div>
							</div>
						</div>
					</div>`
                }
            }

            $('#rooms_content').append(html);
        }
    });
}


function isExistObject(obj) {
    return obj && obj !== 'null' && obj !== 'undefined';
}