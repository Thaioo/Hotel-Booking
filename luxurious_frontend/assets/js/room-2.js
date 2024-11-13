$(document).ready(function () {
    let roomData = JSON.parse(localStorage.getItem('roomData'));
    let savedTotalRoom = localStorage.getItem('totalRoom'); 
    let savedTotalAvaiRoom = localStorage.getItem('totalAvaiRoom'); 

   // if(savedTotalRoom> savedTotalAvaiRoom) {
       
   // }


   var savedTotalRoom1 = Number(savedTotalRoom);
var savedTotalAvaiRoom1 = Number(savedTotalAvaiRoom);

if (savedTotalRoom1 > savedTotalAvaiRoom1) {
   //$('#AvaiRoomMess').text('Số phòng hiện tại không đủ').show();
   //alert('Số phòng hiện tại không đủ')
   Swal.fire({
       icon: 'error',
       title: 'Oops...',
       text: 'Số phòng hiện tại không đủ!',
       footer: '<a href="">Bạn cần hỗ trợ thêm?</a>'
   });
} else {
   //$('#AvaiRoomMess').hide();
}

    console.log(roomData);

    let roomsContainer = $('#roomTypeList');

    roomsContainer.empty();

    roomData.forEach(function (room) {
        let roomCard = `
           <div class="col-xl-4 col-md-6" data-aos="fade-up" data-aos-duration="1500">
               <div class="rooms-card">
                   <img src="${room.image[0]}" alt="room">
                   <div class="details">
                   
                       <h3>${room.roomTypeName}</h3>
                       <h4 style="color:red">${room.numberAvailable} rooms available</h4>
                       <span>$${room.price} / Night</span>
                       <a href="#" class="view-more-btn lh-buttons-2" data-room-id="${room.id}">View More <i class="ri-arrow-right-line"></i></a>
                       <ul>
                           <li><i class="ri-group-line"></i> ${room.capacity} Persons</li>
                           <li><i class="ri-hotel-bed-line"></i> ${room.bedType.name}</li>
                           <li>
   <i class="ri-hotel-line"></i> <!-- Icon hình phòng từ Remix Icon -->
</li>

                           
                           <li><i class="mdi mdi-pool"></i> Swimming Pool</li>
                           <li><i class="ri-wifi-line"></i> Free Wifi</li>
                       </ul>
                       
                   </div>
               </div>
           </div>
       `;
        roomsContainer.append(roomCard);
   });



   $('.view-more-btn').on('click', function (e) {
       e.preventDefault();  

       //localStorage.removeItem('roomData');  
   
        let roomTypeId = $(this).attr('data-room-id');

   
console.log(roomTypeId)

        $.ajax({
           url: `http://localhost:9999/roomType/detail/${roomTypeId}`,   
           type: 'GET',
           success: function (response) {
                console.log('Room details:', response);
   
                localStorage.setItem('roomDetails', JSON.stringify(response));
   
                window.location.href = 'room-details.html';
           },
           error: function (xhr, status, error) {
               
               alert('Failed to retrieve room details. Please try again.');
           }
});
});

});