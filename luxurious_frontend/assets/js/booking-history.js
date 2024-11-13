$(document).ready(function () {
    // Add search event listener
    $('.form_searchPhone').submit(function (e) {
        e.preventDefault();
        searchBooking();

    });

    let params = new URLSearchParams(window.location.search)
    if (params.get('conf')) {
        var jwtJson = parseJwt(params.get('conf'))
        console.log(jwtJson)

        let formData = { 'id':jwtJson.sub, 'token': params.get('conf') }
        confirmEmail(formData)
    }
});

function confirmEmail(formData) {
    console.log(formData)
    $.ajax({
        type: "POST",
        url: "http://localhost:9999/booking/confirm",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(formData),
        success: function (response) {
            if (response.message.includes("not found")) {
                alert(response.message)
            } else if (response.message.includes("not available")) {
                alert(response.message)
            } else {
                $('#bookingHistory').append("YOUR BOOKING HAS BEEN CONFIRMED! YOU CAN CHECK YOUR BOOKING INFORMATION BY PHONE NUMBER.");
            }
        },
        error: function (response) {
            if (response.responseJSON.message.includes("JWT expired")) {
                alert("Confirmation link expired! Please check your booking status here!")
            } 
            else {
                alert(response.responseJSON.message)
            } 
        }
    });
    var url = document.location.href;
    window.history.pushState({}, "", url.split("?")[0]);
    
}

function convertRoomCol(roomTypes) {
    let roomNo = ''

    for (const key in roomTypes) {

        let roomsString = ''
        roomTypes[key].forEach(room => {
            roomsString += room.name + ', '
        });
        roomNo += `<span>${key} : </span>${roomsString}`
    }
    return roomNo.substring(roomNo, roomNo.length - 2)
}

// Function to search booking by phone number (currently demo)
function searchBooking() {
    const searchPhone = $('#searchPhone').val();

    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/booking/p" + searchPhone,
        success: function (response) {
            if (response.statusCode == 200 && response.data.length > 0) {
                renderBookings(response.data)
            } else {
                alert("This phone don't have any bookings!")
            }
        },
        error: function (response) {
            alert(response.responseJSON.message)
        }
    })

}

// Function to render bookings
function renderBookings(bookings) {
    const bookingHistory = $('#bookingHistory');
    bookingHistory.empty(); // Clear any existing content

    bookings.forEach(booking => {
        const bookingItem = `
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="heading${booking.id}">
                                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${booking.id}" aria-expanded="true" aria-controls="collapse${booking.id}">
                                    <span class="left">${booking.checkIn} to ${booking.checkOut}</span>
                                    <span style="color:${getColorOfStatus(booking.bookingStatus.name)}" class="right">${booking.bookingStatus.name}</span>
                                </button>
                            </h2>
                            <div id="collapse${booking.id}" class="accordion-collapse collapse" aria-labelledby="heading${booking.id}" data-bs-parent="#bookingHistory">
                                <div class="accordion-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <ul class="list-group">
                                                <li class="list-group-item"><strong>Booking ID:</strong> ${booking.id}</li>
                                                <li class="list-group-item"><strong>Check-in:</strong> ${booking.checkIn}</li>
                                                <li class="list-group-item"><strong>Check-out:</strong> ${booking.checkOut}</li>
                                                <li class="list-group-item"><strong>Rooms:</strong> ${convertRoomCol(booking.roomTypes)}</li>
                                            </ul>
                                        </div>
                                        <div class="col-md-6">
                                            <ul class="list-group">
                                                <li class="list-group-item"><strong>Number of Adult:</strong> ${booking.adultNo}</li>
                                                <li class="list-group-item"><strong>Number of Children:</strong> ${booking.childrenNo}</li>
                                                <li class="list-group-item"><strong>Payment Status:</strong> ${booking.paymentStatus.name}</li>
                                                <li class="list-group-item"><strong>Paid Amount:</strong> $${booking.paidAmount}</li>
                                                <li class="list-group-item"><strong>Total Amount:</strong> $${booking.total}</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
        bookingHistory.prepend(bookingItem);
    });
}

function getColorOfStatus(status) {
    color = ''
    switch (status) {
        case 'Pending Confirmation':
            color = '#ffa75b'
            break;
        case 'Confirmed':
            color = '#25c6ef'
            break;
        case 'Checked In':
            color = '#38cb38'
            break;
        case 'Checked Out':
            color = '#777'
            break;
        case 'Cancelled':
            color = '#f50404'
            break;
        default:
            break;
    }
    return color;
}

function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}