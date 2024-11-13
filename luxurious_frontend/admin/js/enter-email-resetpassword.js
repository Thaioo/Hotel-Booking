$(document).ready(function () { 

    $('#reset-password').click(function (e) { 
        e.preventDefault();

        var email = $('#email-reset').val();
        console.log(email);

        $.ajax({
            url: "http://localhost:9999/email/resetpassword/" + email, // Đưa email vào URL
            method: "GET",
            dataType: "json",
        }).done(function(item){
            if (item.statusCode === 200 && item.message === "Email sent, please check your inbox." ) {

                alert(item.message);

            }else if (item.statusCode === 200 && item.message === "Email not found" ) {

                alert(item.message);

            } else {
                alert("Đã có lỗi xảy ra, vui lòng thử lại.");
            }
    
        }).fail(function(jqXHR, textStatus, errorThrown) {
            alert("Có lỗi xảy ra: " + textStatus + ": " + errorThrown);
        });
        
    });

});