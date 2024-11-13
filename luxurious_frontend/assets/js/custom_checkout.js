$(document).ready(function (e) {
    let searchParams = new URLSearchParams(window.location.search)
    $('#input_checkin').val(searchParams.get('in'));
    $('#input_checkout').val(searchParams.get('out'));
    $('#input_adult').val(searchParams.get('adult'));
    $('#input_children').val(searchParams.get('children'));
    
    if (searchParams.get('rooms')) {
        var selectedRooms = searchParams.get('rooms').split(',')
    }

    var inputDateRange = {}
    inputDateRange.checkIn = $('#input_checkin').val();
    inputDateRange.checkOut = $('#input_checkout').val();
    inputDateRange.adultNumber = $('#input_adult').val();
    inputDateRange.childrenNumber = $('#input_children').val();

    showAllRooms(selectedRooms)
    // loadAvailableRooms(inputDateRange)


    $('#load_rooms').click(function (e) {
        e.preventDefault();
        var inputDateRange = {}
        inputDateRange.checkIn = $('#input_checkin').val();
        inputDateRange.checkOut = $('#input_checkout').val();
        inputDateRange.adultNumber = $('#input_adult').val();
        inputDateRange.childrenNumber = $('#input_children').val();
        loadAvailableRooms(inputDateRange)
    });

    $('#submit_booking').click(function (e) {
        e.preventDefault()
        var inputBooking = {}
        inputBooking.checkInDate = $('#input_checkin').val();
        inputBooking.checkOutDate = $('#input_checkout').val();
        inputBooking.rooms = $('#input_rooms').val();
        inputBooking.roomName = $('#input_rooms option:selected').toArray().map(item => item.text)
        inputBooking.adultNumber = $('#input_adult').val();
        inputBooking.childrenNumber = $('#input_children').val();
        inputBooking.idPayment = $('#input_payment_method').val();
        inputBooking.total = $('#input_total').text().substring(1);

        inputBooking.firstName = $('#input_first_name').val();
        inputBooking.lastName = $('#input_last_name').val();
        inputBooking.phone = $('#input_phone').val();
        inputBooking.email = $('#input_email').val();
        inputBooking.address = $('#input_address').val();
        
        addBooking(inputBooking)
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

    $("#load_info").click(function (e) { 
        e.preventDefault();
        let phone = $('#input_return_phone').val();

        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: "http://localhost:9999/user/p" + phone,
            success: function (response) {
                if (response.statusCode == 200 && response.message) {
                    alert(response.message)
                } else {
                    let data = response.data
                    $('#input_first_name').val(data.firstName);
                    $('#input_last_name').val(data.lastName);
                    $('#input_phone').val(data.phone);
                    $('#input_email').val(data.email);
                    $('#input_address').val(data.address);

                }
            },
            error: function (response) {
                alert(response.responseJSON.message)
            }
        })        
    });


});

function addBooking(inputAddBooking) {
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/booking",
        data: JSON.stringify(inputAddBooking),
        success: function (response) {
            console.log(response);
            
            if (response.statusCode == 200) {
                // alert("Please check your email to confirm booking!")
                inputAddBooking.idBooking = response.data.id
                sendConfirmEmail(inputAddBooking)
                alert(`Add new booking successfully! Please check email ${inputAddBooking.email} to confirm booking within 24 hours`)
                window.location.href = `booking-history.html`
            }
        },
        error: function (response) {
            alert(response.responseJSON.message)
        }
    });
}

function sendConfirmEmail(inputAddBooking) {
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/email/sendBookingInfoToQueue",
        data: JSON.stringify(inputAddBooking),
        success: function (response) {
            console.log(response);
        },
        error: function (response) {
            console.log(response);
        }
    });
    // $('#bookingHistory').append(`PLEASE CHECK YOUR EMAIL (${params.get('email')}) TO CONFIRM BOOKING WITHIN 24 HOURS!`);
}

function showAllRooms(selectedRooms) {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/status",
        success: function (response) {

            response.data.listPaymentMethod.forEach(item => {
                $("#input_payment_method").append(`<option value="${item.id}">${item.name}</option>`);
            });

            // show all rooms
            html = ''
            for (let i = 0; i < response.data.listRoomType.length; i++) {
                let itemRoomType = response.data.listRoomType[i]
                html += `<optgroup label="${itemRoomType.name} (${itemRoomType.price}$/night, ${itemRoomType.capacity} person)">`
                for (let j = 0; j < itemRoomType.rooms.length; j++) {
                    let room = itemRoomType.rooms[j]
                    html += `<option disabled="true" price="${itemRoomType.price}" value="${room.id}">${room.name}</option>`
                }
                html += `</optgroup>`
            }

            $('#input_rooms').append(html);
            
            //Nếu selected rooms có trên url params
            if (selectedRooms) {
                $('#input_rooms').val(selectedRooms);
                selectedRooms.forEach(room => {
                    $("#input_rooms").find(`option[value=${room}]`).removeAttr('disabled')
                })
            }

            //Reload chosen select
            $('#input_rooms').trigger('chosen:updated')
            $('#input_payment_method').trigger("chosen:updated");
            calculateTotal()

        }
    }).done(function (params) {

    });
}

function loadAvailableRooms(inputDateRange) {

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/room",
        data: JSON.stringify(inputDateRange),
        success: function (response) {

            $('#input_rooms').val('')
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
    $('#input_total').html('$' + total);

}