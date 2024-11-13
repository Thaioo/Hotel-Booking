var token = localStorage.getItem('jwt');

$(document).ready(function() {
    $('#addRoomTypeForm').on('submit', function(event) {
        event.preventDefault();  

        var formData = new FormData(this);  



 
        $.ajax({
            url: "http://localhost:9999/roomType",  
            method: "POST",
            data: formData,
            headers: {
                "Authorization": "Bearer " + token  // Truyền token vào header
            },
            processData: false,  
            contentType: false,  
            success: function(response) {
                if (response.statusCode === 200) {
                    alert('Room type added successfully!');
                } else {
                    alert('Failed to add room type: ' + response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error("Status: " + status);
                console.error("Error: " + error);
                console.error("Response: " + xhr.responseText);
                alert('There was an error submitting the form.');
            }
        });
    });
});