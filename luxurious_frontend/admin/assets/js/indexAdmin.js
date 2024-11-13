// $(document).ready(function () {
//     var startDate = $('#dateRange').data('daterangepicker').startDate.format('YYYY-MM-DD')
//     var endDate = $('#dateRange').data('daterangepicker').endDate.format('YYYY-MM-DD');

//     showStatus(startDate, endDate)

    
//     $('#dateRange').on('apply.daterangepicker', function (e, picker) {
//         e.preventDefault();
//         var startDate = $('#dateRange').data('daterangepicker').startDate.format('YYYY-MM-DD')
//         var endDate = $('#dateRange').data('daterangepicker').endDate.format('YYYY-MM-DD');
//         showStatus(startDate, endDate)
//     });
// });


// function showStatus(startDate, endDate) {
//     // startDate = '2024-10-15'
//     // endDate = '2024-10-20'
//     $.ajax({
//         type: "GET",
//         contentType: "application/json; charset=utf-8",
//         url: `http://localhost:9999/dashboard?startDate=${startDate}&endDate=${endDate}`,
//         headers: {
//             "Authorization": "Bearer " + token  // Truyền token vào header
//         },
//         success: function (response) {
//             console.log(response);
//             $('#dashboard_visitor').html(response.totalVisitor);
//             $('#dashboard_visitor_growth b').html(response.growthVisitor+'%');            
//             changeGrowthClass(response.growthVisitor, 'dashboard_visitor_growth')
        
//         }
//     });
// }


// function changeGrowthClass(rate, elementId) {
//     if (rate >= 0) {
//         $(`#${elementId}`).removeClass("down").addClass("up");
//         $(`#${elementId} i`).removeClass("ri-arrow-down-line").addClass("ri-arrow-up-line");
//     } else {
//         $(`#${elementId}`).removeClass("up").addClass("down");
//         $(`#${elementId} i`).removeClass("ri-arrow-up-line").addClass("ri-arrow-down-line");
//     }
// }