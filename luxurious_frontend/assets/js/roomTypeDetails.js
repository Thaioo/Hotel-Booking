
$(document).ready(function () {
    let roomDetails = JSON.parse(localStorage.getItem('roomDetails'));



    console.log(roomDetails);

    $('#nameRoomType').text(roomDetails.data.name);  
   $('.lh-room-details-contain p').text(roomDetails.data.overview || "No overview available.");  
   $('#roomTypePrice').text(roomDetails.data.price + " $" || "No Price available.");

    if (roomDetails.data.amenity) {
       let amenitiesArray = roomDetails.data.amenity.split(',');  

        let amenitiesHTML = '';
       amenitiesArray.forEach(function (amenity) {
           amenitiesHTML += `<li><code>*</code> ${amenity.trim()}</li>`;
       });
 
        $('.lh-room-details-amenities .row').html(amenitiesHTML);
   } else {
       // Nếu không có tiện ích, hiển thị thông báo
       $('.lh-room-details-amenities .row').html('<h4 class="lh-room-inner-heading">Amenities</h4><p>No amenities available.</p>');
   }

 
//     if (roomDetails.data.roomName && roomDetails.data.roomName.length > 0) {
//        let roomNameHTML = '<h4 class="lh-room-inner-heading">Available Rooms</h4>';
//        roomDetails.data.roomName.forEach(function (room, index) {
//             roomNameHTML += `<li class="shake shake-${index}">${room}</li>`;
//        });
//        $('#availableRoomList').html(roomNameHTML);
//    } else {
//        $('#availableRoomList').html('<li>No rooms available</li>');
//    }

   $('#bedType').text(`${roomDetails.data.bedName}`); 


   if (roomDetails.data.image && roomDetails.data.image.length > 0) {
    $('#largeRoomImage').attr('src', roomDetails.data.image[0]);

    let thumbnailHTML = '';
   roomDetails.data.image.forEach(function (imageUrl, index) {
       thumbnailHTML += `
           <img src="${imageUrl}" alt="Room ${index + 1}" onclick="changeImage('${imageUrl}')">
       `;
   });

    $('#thumbnailGallery').html(thumbnailHTML);
} else {
    $('#largeRoomImage').attr('src', 'assets/img/no-image.png');
   $('#thumbnailGallery').html('<p>No images available</p>');
}


});

function changeImage(newImageUrl) {
   $('#largeRoomImage').attr('src', newImageUrl);
}


$(document).ready(function(){
   // Slider chính
   $('.slider-for').slick({
       slidesToShow: 1,
       slidesToScroll: 1,
       arrows: false,
       fade: true,
       asNavFor: '.slider-nav'
   });

   // Slider ảnh nhỏ
   $('.slider-nav').slick({
       slidesToShow: 4,
       slidesToScroll: 1,
       asNavFor: '.slider-for',
       dots: false,
       centerMode: true,
       focusOnSelect: true
   });
});




