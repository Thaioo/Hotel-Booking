var token = localStorage.getItem('jwt');

$(document).ready(function () {
    //KHỞI TẠO CHOSEN
    $('.chosen-select').chosen({
        no_results_text: "Oops, nothing found!", // Message displayed when no results match
        placeholder_text_multiple: "Select rooms" // Placeholder text for multiple selects
    });

    showBooking()
    showStatus()
    // showAllRooms();

    $('#submit_booking').click(function (e) {
        var inputBooking = {}


        inputBooking.checkInDate = $('#input_checkin').val();
        inputBooking.checkOutDate = $('#input_checkout').val();
        inputBooking.rooms = $('#input_rooms').val();
        inputBooking.adultNumber = $('#input_adult').val();
        inputBooking.childrenNumber = $('#input_children').val();
        inputBooking.idBookingStatus = $('#input_booking_status').val();
        inputBooking.idPaymentStatus = $('#input_payment_status').val();
        inputBooking.idPayment = $('#input_payment_method').val();
        inputBooking.paidAmount = $('#input_paid_amount').val();
        inputBooking.total = $('#input_total').val();

        var idBooking = $('#booking_details').attr('idBooking')
        if (idBooking == "") {
            inputBooking.firstName = $('#input_first_name').val();
            inputBooking.lastName = $('#input_last_name').val();
            inputBooking.phone = $('#input_phone').val();
            inputBooking.email = $('#input_email').val();
            inputBooking.address = $('#input_address').val();
            inputBooking.roomName = $('#input_rooms option:selected').toArray().map(item => item.text)

            addBooking(inputBooking)
        } else {
            inputBooking.idGuest = $('.guest-profile').attr('id_guest');
            inputBooking.idBooking = idBooking
            updateBooking(inputBooking)
        }
    });

    $('#load_rooms').click(function (e) {
        e.preventDefault();
        var inputDateRange = {}
        inputDateRange.checkIn = $('#input_checkin').val();
        inputDateRange.checkOut = $('#input_checkout').val();
        inputDateRange.adultNumber = $('#input_adult').val();
        inputDateRange.childrenNumber = $('#input_children').val();
        loadAvailableRooms(inputDateRange)
    });

    $('#clear_booking').click(function (e) {
        e.preventDefault();
        clearAll()
    });

    $('body').on('click', '.update_booking', function (e) {
        e.preventDefault();

        var booking = JSON.parse($(this).closest('tr').attr('booking_data'));
        showDetailBooking(booking)
        $('#booking_details').attr('idBooking', booking.id)
    });

    $('body').on('click', '.delete_booking', function (e) {
        e.preventDefault();
        
        var idBooking = $(this).attr("idBooking");

        if (confirm('Are you sure?')) {
            deleteBooking(idBooking)

        }
    });

    $('#reload_booking').click(function (e) {
        $('#booking_table').DataTable().clear()
        e.preventDefault();
        showBooking()
    });

    $('#input_rooms').on('change', function () {
        calculateTotal()
    });

    $('#input_checkin').on('change', function () {
        calculateTotal()
    });


    $('#input_checkout').on('change', function () {
        calculateTotal()
    });



});

function calculateTotal() {
    const checkInDate = new Date(document.getElementById("input_checkin").value);
    const checkOutDate = new Date(document.getElementById("input_checkout").value);
    var nights = (checkOutDate - checkInDate) / (1000 * 3600 * 24)
    var listPrice = $('#input_rooms').find('option:selected').map(function () {
        return parseFloat($(this).attr('price'));
    }).get()
    var total = listPrice.reduce(function (total, price) {
        return total + (price * nights)
    }, 0);
    $('#input_total').val(total);
}

function deleteBooking(id) {
    $.ajax({
        type: "DELETE",
        contentType: "application/json; charset=utf-8",
        url: `http://localhost:9999/booking/${id}`,
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function (response) {
            if (response.statusCode == 200) {
                alert(response.message)
                $('#booking_table').DataTable().row($(`.delete_booking[idBooking=${id}]`).parents("tr")).remove().draw(false)

            }

        }
    });
}

function updateBooking(inputEditBooking) {
    console.log(inputEditBooking)
    $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/booking",
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        data: JSON.stringify(inputEditBooking),
        success: function (response) {
            if (response.statusCode == 200) {
                alert(response.message)
                clearAll()
            }
        },
        error: function (response) {
            console.log(response)
            alert(response.responseJSON.message)
        }
    });
}

function showDetailBooking(booking) {

    $('#input_first_name').val(booking.firstName);
    $('#input_first_name').prop('disabled', true);

    $('#input_last_name').val(booking.lastName);
    $('#input_last_name').prop('disabled', true);

    $('#input_phone').val(booking.phone);
    $('#input_phone').prop('disabled', true);

    $('#input_email').val(booking.email);
    $('#input_email').prop('disabled', true);

    $('#input_address').val(booking.address);
    $('#input_address').prop('disabled', true);

    $('#input_checkin').val(booking.checkIn);
    $('#input_checkout').val(booking.checkOut);
    $('#input_adult').val(booking.adultNo);
    $('#input_children').val(booking.childrenNo);
    $('#input_booking_status').val(booking.bookingStatus.id);
    $('#input_payment_status').val(booking.paymentStatus.id);
    $('#input_payment_method').val(booking.paymentMethod.id);
    $('#input_paid_amount').val(booking.paidAmount);
    $('#input_total').val(booking.total);
    $('.guest-profile').attr('id_guest', booking.idGuest);
    $('#input_rooms').find('option').attr('disabled', 'true')

    var listRoomId = []
    for (const key in booking.roomTypes) {
        booking.roomTypes[key].forEach(room => {
            $("#input_rooms").find(`option:contains(${room.name})`).removeAttr('disabled')
            listRoomId.push(room.id)
        })
    }

    $('#input_rooms').val(listRoomId);

    $('#input_rooms').trigger('chosen:updated')

    window.scrollTo({ top: 0, behavior: 'smooth' });

}

function clearAll() {
    $('input').val('');
    $('input').prop('disabled', false);
    $('textarea').prop('disabled', false);
    $('textarea').val('');
    $('#booking_details').attr('idBooking', '')
    $('#input_rooms').val('');
    $('#input_rooms').find('option').attr('disabled', 'true')
    $('#input_rooms').trigger('chosen:updated')
}

function addBooking(inputAddBooking) {
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/booking",
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        data: JSON.stringify(inputAddBooking),
        success: function (response) {
            if (response.statusCode == 200) {
                alert(response.message)
                clearAll()
            }
        },
        error: function (response) {
            alert(response.responseJSON.message)
        }
    });
}
function loadAvailableRooms(inputDateRange) {

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/room",
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        data: JSON.stringify(inputDateRange),
        success: function (response) {

            $('#input_rooms').find('option').attr('disabled', 'true')
            $("#input_rooms").find(`option:selected`).removeAttr('disabled')

            for (let i = 0; i < response.data.length; i++) {
                let itemRoomType = response.data[i]
                for (let j = 0; j < itemRoomType.roomAvailableDTOList.length; j++) {
                    let room = itemRoomType.roomAvailableDTOList[j]
                    $("#input_rooms").find(`option:contains(${room.roomName})`).removeAttr('disabled')
                }
            }
            $('#input_rooms').trigger("chosen:updated");

        },
        error: function (response) {
            alert(response.responseJSON.message)
        }
    });
}

function showAllRooms() {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/roomType",
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function (response) {
            html = ''
            for (let i = 0; i < response.data.length; i++) {
                let itemRoomType = response.data[i]
                html += `<optgroup label="${itemRoomType.name} (${itemRoomType.price}$/night, ${itemRoomType.capacity} person)">`
                for (let j = 0; j < itemRoomType.roomName.length; j++) {
                    let roomName = itemRoomType.roomName[j]
                    html += `<option disabled="true" value="${roomName}">${roomName}</option>`
                }
                html += `</optgroup>`
            }

            $('#input_rooms').append(html).trigger("chosen:updated");
        }
    });
}

function showStatus() {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/status",
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function (response) {
            response.data.listPaymentStatus.forEach(item => {
                $("#input_payment_status").append(`<option value="${item.id}">${item.name}</option>`);
            });

            response.data.listPaymentMethod.forEach(item => {
                $("#input_payment_method").append(`<option value="${item.id}">${item.name}</option>`);
            });

            response.data.listBookingStatus.forEach(item => {
                $("#input_booking_status").append(`<option value="${item.id}">${item.name}</option>`);
            });

            // show all rooms
            html = ''
            for (let i = 0; i < response.data.listRoomType.length; i++) {
                let itemRoomType = response.data.listRoomType[i]
                html += `<optgroup label="${itemRoomType.name} (${itemRoomType.price}$/night)">`
                for (let j = 0; j < itemRoomType.rooms.length; j++) {
                    let room = itemRoomType.rooms[j]
                    html += `<option disabled="true" price="${itemRoomType.price}" value="${room.id}">${room.name}</option>`
                }
                html += `</optgroup>`
            }

            $('#input_rooms').append(html).trigger("chosen:updated");
        }
    });
}

function showBooking() {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/booking",
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function (response) {

            var dt = $('#booking_table').DataTable();
            dt.destroy();

            for (let i = 0; i < response.data.length; i++) {
                var item = response.data[i]
                var htmlRow =
                    `<tr booking_data='${JSON.stringify(item)}'>
                    <td class="token">${item.id}</td>
                    <td><span class="name">${item.firstName} ${item.lastName}</span>
                    </td>
                    <td>${item.checkIn}</td>
                    <td>${item.checkOut}</td>
                    <td class="${getClassOfStatus(item.bookingStatus.name)}">${item.bookingStatus.name}</td>
                    <td class="${getClassOfStatus(item.paymentStatus.name)}">${item.paymentStatus.name}</td>
                    <td>$${item.paidAmount}</td>
                    <td><b>$${item.total}</b></td>
                    <td class="type">${convertRoomCol(item.roomTypes)}</td>
                    <td class="rooms">
                        <span class="mem">${item.adultNo} Adult</span> /
                        <span class="room">${item.childrenNo} Children</span>
                    </td>
                    <td>
                        <div class="d-flex justify-content-center">
                        
                            <button type="button"
                                class="btn btn-outline-success dropdown-toggle dropdown-toggle-split"
                                data-bs-toggle="dropdown" aria-haspopup="true"
                                aria-expanded="false" data-display="static">
                                <span class="sr-only"><i
                                        class="ri-settings-3-line"></i></span>
                            </button>
                            <div class="dropdown-menu">
                                <a idBooking="${item.id}" class="dropdown-item update_booking" href="#">Edit</a>
                                <a idBooking="${item.id}" class="dropdown-item delete_booking" href="#">Delete</a>
                            </div>
                        </div>
                    </td>
                </tr>`

                $('#booking_table tbody').append(htmlRow);
            }

            $('#booking_table').DataTable();
        }
    });
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


function getClassOfStatus(status) {
    let className = ''
    switch (status) {
        case 'Pending Confirmation':
            className = 'pending'
            break;
        case 'Confirmed':
            className = 'confirmed'
            break;
        case 'Checked In':
            className = 'checked_in'
            break;
        case 'Checked Out':
            className = 'checked_out'
            break;
        case 'Cancelled':
            className = 'cancelled'
            break;
        case 'Pending':
            className = 'pending'
            break;
        case 'Completed':
            className = 'completed'
            break;
        case 'Failed':
            className = 'failed'
            break;
        case 'Refunded':
            className = 'refunded'
            break;
        case 'Partially Paid':
            className = 'partially_paid'
            break;
        case 'cancelled':
            className = 'cancelled'
            break;
        case 'awaiting_confirmation':
            className = 'awaiting_confirmation'
            break;
        default:
            break;
    }
    return className;
}