

$(document).ready(function () {
    $('.result-placeholder').click(function (e) {
        e.preventDefault();

        let checkIn = $('#checkIn').val();
        let checkOut = $('#checkOut').val();
        let adults = $('#adults').val();
        let children = $('#children').val();


        let parsedAdults = parseInt(adults) || 0;
        let parsedChildren = parseInt(children) || 0;
        let totalGuest = parsedAdults + parsedChildren;

        //  if (!checkIn || !checkOut || !adults || children === '') {
        //     alert("Please fill in all fields");
        //     return;
        // }

        let formattedCheckIn = new Date(checkIn).toISOString().split('T')[0];
        let formattedCheckOut = new Date(checkOut).toISOString().split('T')[0];


        // if (formattedCheckIn > formattedCheckOut) {
        //     alert("Invalid check-in date. Check-in date must be less than or equal to check-out date.");

        // } 



        let requestData = {
            "checkIn": formattedCheckIn,
            "checkOut": formattedCheckOut,
            "adultNumber": adults,
            "childrenNumber": children
        };

        console.log(requestData);

        $.ajax({
            url: 'http://localhost:9999/room',
            type: 'GET',
            data: requestData, // Send form-data
            success: function (response) {


                console.log("Response from API:", response.data);

                let totalAvailableRooms = 0;

                response.data.forEach(function (room) {
                    totalAvailableRooms += room.numberAvailable;
                });

                // Store the room data in localStorage
                localStorage.setItem('roomData', JSON.stringify(response.data));
                localStorage.setItem('totalRoom', totalGuest); totalAvailableRooms
                localStorage.setItem('totalAvaiRoom', totalAvailableRooms);
                localStorage.setItem('checkIn', formattedCheckIn)
                localStorage.setItem('checkOut', formattedCheckOut)
                localStorage.setItem('adult', adults)
                localStorage.setItem('children', children)

                // Redirect the user to the room-2.html page
                window.location.href = 'room-2.html';
            },
            error: function (error) {
                if (error.responseJSON && error.responseJSON.message.includes("Check-in date must be before")) {
                    alert('Check-in date must be before check-out date');
                }


                console.error(error);
                //alert("Error: Could not retrieve room data.");
            }
        });
    });
});






