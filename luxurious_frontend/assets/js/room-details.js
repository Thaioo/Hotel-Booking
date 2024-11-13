$(document).ready(function (e) {


    let searchParams = new URLSearchParams(window.location.search)
    $('#input_checkin').val(searchParams.get('in'));
    $('#input_checkout').val(searchParams.get('out'));
    $('#input_adult').val(searchParams.get('adult'));
    $('#input_children').val(searchParams.get('children'));

    var inputDateRange = {}
    inputDateRange.checkIn = $('#input_checkin').val();
    inputDateRange.checkOut = $('#input_checkout').val();
    inputDateRange.adultNumber = $('#input_adult').val();
    inputDateRange.childrenNumber = $('#input_children').val();

    showAllRooms()
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

    $('#input_rooms').on('change', function () {
        calculateTotal()
    });

    $('#input_checkin').on('change', function () {
        calculateTotal()
    });


    $('#input_checkout').on('change', function () {
        calculateTotal()
    });

    $('.form_book_now').submit(function (e) { 
        e.preventDefault()
        checkIn = $('#input_checkin').val();
        checkOut = $('#input_checkout').val();
        adult = $('#input_adult').val();
        children = $('#input_children').val();
        rooms = $('#input_rooms').val()

        window.location.href = `checkout.html?in=${checkIn}&out=${checkOut}&rooms=${rooms}&adult=${adult}&children=${children}`
        
    });


    
});

function showAllRooms() {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:9999/status",
        success: function (response) {
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

            $('#input_rooms').append(html).trigger("chosen:updated");
            $('#input_payment_method').trigger("chosen:updated");

            // loadAvailableRooms(inputDateRange)

        }
    })
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
    $('#input_total').html('$'+total);





}