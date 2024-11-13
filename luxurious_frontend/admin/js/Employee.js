// $(document).ready(function () {

//     $('#userAdd').click(async function(event){
//         // Ngăn hành vi mặc định của form khi click vào button tránh mất giá trị vừa nhập khi click mà giá trị sai
//         event.preventDefault();
//         console.log('con meo')
  
//         // Tạo một đối tượng FormData
//         var formData = new FormData();
  
//         // Thêm các giá trị từ form vào đối tượng FormData
//         formData.append('username', $('#username').val());
//         formData.append('password', $('#password').val());
//         formData.append('firstname', $('#firstname').val());
//         formData.append('lastname', $('#lastname').val());
//         formData.append('dob', $('#dob').val());
//         formData.append('phone', $('#phone').val());
//         formData.append('email', $('#email').val().toLowerCase());
//         formData.append('address', $('#address').val());
//         formData.append('image',$('#imageUpload').val());
//         formData.append('summary', $('#summary').val());
//         formData.append('role', $('#id_role').val());
      
//         // Kiểm tra dữ liệu
//         var isValid = true;
//         var message = "";
  
//         // Kiểm tra fullname (không chứa số hoặc ký tự đặc biệt)
//         var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
//         if (!$('#firstname').val().match(nameRegex) || !$('#lastname').val().match(nameRegex)) {
//             message += "Họ tên không được chứa số hoặc ký tự đặc biệt.\n";
//             isValid = false;
//         }
        
//         // Kiểm tra phone (chỉ chứa số)
//         var phoneRegex = /^\d+$/;
//         if (!$('#phone').val().match(phoneRegex)) {
//             message += "Số điện thoại chỉ được chứa số.\n";
//             isValid = false;
//         }
  
//         // Kiểm tra định dạng dob (yyyy-mm-dd)
//         var dobRegex = /^\d{4}-\d{2}-\d{2}$/;
//         if (!$('#dob').val().match(dobRegex)) {
//             message += "Định dạng ngày sinh phải là yyyy-mm-dd.\n";
//             isValid = false;
//         }
  
//         // Kiểm tra định dạng email
//         var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//         if (!$('#email').val().match(emailRegex)) {
//             message += "Email chưa đúng định dạng.\n";
//             isValid = false;
//         }
  
//         try {
//             var checkEmail = await checkEmailExistence($('#email').val().toLowerCase());
//             if (checkEmail === true) {
//                 message += "Email đã tồn tại.\n";
//                 isValid = false;
//             }
//         } catch (error) {
//             alert("Lỗi kiểm tra email: " + error);
//             return;
//         }
  
//         try {
//             var checkPhone = await checkPhoneExistence($('#phone').val());
//             if (checkPhone === true) {
//                 message += "Số điện thoại đã tồn tại.\n";
//                 isValid = false;
//             }
//         } catch (error) {
//             alert("Lỗi kiểm tra số điện thoại: " + error);
//             return;
//         }

//         console.log('con meo')

//         console.log(formData)
  
//         if (!isValid) {
//             alert(message);  // Hiện thông báo lỗi
//         } else {
//             console.log(formData)
//             // Thực hiện yêu cầu AJAX với FormData
//             $.ajax({
//                 url: "http://localhost:9999/employee",
//                 method: "POST",
//                 data: formData,
//                 processData: false, // Không xử lý dữ liệu thành chuỗi
//                 contentType: false, // Không đặt Content-Type (để mặc định là multipart/form-data)
//             }).done(function(itzem){
//                 if (item.statusCode === 200) {
//                     alert("Thêm người dùng thành công!");
//                     // Bạn có thể reset form hoặc thực hiện hành động khác
//                     $('#username').val('');
//                     $('#password').val('');
//                     $('#firstname').val('');
//                     $('#lastname').val('');  // Xóa giá trị trong các ô input
//                     $('#dob').val('');
//                     $('#phone').val('');
//                     $('#email').val('');
//                     $('#address').val('');
//                     $('#summary').val('');
//                     $('#imageUpload').val('');
//                     $('#id_role').val('');
//                 } else {
//                     alert("Đã có lỗi xảy ra, vui lòng thử lại.");
//                 }
        
//             }).fail(function(jqXHR, textStatus, errorThrown) {
//                 alert("Có lỗi xảy ra: " + textStatus + ": " + errorThrown);
//             });
//         }
//     });
  
//   });


//   async function checkEmailExistence(email) {
//       let checkEmail = false;
//       await $.ajax({            
//           url: "http://localhost:9999/user/checkemail?email=" + encodeURIComponent(email),
//           method: "POST",
//           contentType: "application/x-www-form-urlencoded"
//       }).done(function(item){
//           checkEmail = item.data;
//           console.log("tra ve?: "+item);
//           console.log(item.data);
//       }).fail(function(jqXHR, textStatus, errorThrown) {
//           console.log("Lỗi kiểm tra email: " + textStatus + ": " + errorThrown);
//       });
//       return checkEmail;
//   }

//   async function checkPhoneExistence(phone) {
//       let checkPhone = false;
//       await $.ajax({
//           url: "http://localhost:9999/user/checkphone?phone=" + encodeURIComponent(phone),
//           method: "POST",
//           contentType: "application/x-www-form-urlencoded"
//       }).done(function(item){
//           checkPhone = item.data;
//           console.log("Phone exists?: " + item);
//           console.log(item.data);
//       }).fail(function(jqXHR, textStatus, errorThrown) {
//           console.log("Error checking phone: " + textStatus + ": " + errorThrown);
//       });
//       return checkPhone;
//   }

//   async function checkUsernameExistence(username) {
//     let checkusername = false;
//     await $.ajax({
//         url: "http://localhost:9999/employee/checkusername/username=" + encodeURIComponent(phone),
//         method: "POST",
//         contentType: "application/x-www-form-urlencoded"
//     }).done(function(item){
//         checkPhone = item.data;
//         console.log("username exists?: " + item);
//         console.log(item.data);
//     }).fail(function(jqXHR, textStatus, errorThrown) {
//         console.log("Error checking username : " + textStatus + ": " + errorThrown);
//     });
//     return checkusername;
// }

//   // Đoạn mã tự động thêm dấu gạch ngang vào ngày sinh
//   document.getElementById('dob').addEventListener('input', function(e) {
//       // Lấy giá trị hiện tại trong trường input
//       let input = e.target.value.replace(/\D/g, '');  // Thay đổi này giúp loại bỏ ký tự không phải số

//       // Xóa tất cả các dấu gạch ngang nếu có
//       input = input.replace(/-/g, '');

//       // Kiểm tra nếu độ dài chuỗi đủ để chèn dấu gạch ngang
//       if (input.length > 4 && input.length <= 6) {
//           // Chèn dấu gạch ngang giữa năm và tháng
//           input = input.slice(0, 4) + '-' + input.slice(4);
//       } else if (input.length > 6) {
//           // Chèn dấu gạch ngang giữa năm, tháng và ngày
//           input = input.slice(0, 4) + '-' + input.slice(4, 6) + '-' + input.slice(6);
//       }

//       // Cập nhật giá trị của trường input với dấu gạch ngang
//       e.target.value = input;
      
//   });   

$(document).ready(function () {
    var token = localStorage.getItem('jwt');
    $('#userAdd').click(function (e) {
        e.preventDefault(); // Prevent form from submitting the traditional way

        // Create a FormData object to hold the form data including file
        var formData = new FormData();
        formData.append('firstName', $('#firstname').val());
        formData.append('username', $('#username').val());
        formData.append('password', $('#password').val());
        formData.append('Idrole', $('#id_role').val());
        formData.append('lastName', $('#lastname').val());
        formData.append('dob', $('#dob').val());
        formData.append('phone', $('#phone').val());
        formData.append('email', $('#email').val());
        formData.append('address', $('#address').val());
        formData.append('summary', $('#profile_detail').val());

        // Handle image file if it is uploaded
        var imageFile = $('#imageUpload')[0].files[0];
        if (imageFile) {
            formData.append('image', imageFile);
        }

        // Kiểm tra dữ liệu
        var isValid = true;
        var message = "";

        // Kiểm tra phone (chỉ chứa số)
        var phoneRegex = /^\d+$/;
        if (!$('#phone').val().match(phoneRegex)) {
            message += "Số điện thoại chỉ được chứa số.\n";
            isValid = false;
        }

                // Kiểm tra first name last name (không chứa số hoặc ký tự đặc biệt)
        var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
        if (!$('#firstname').val().match(nameRegex) && !$('#lastname').val().match(nameRegex)) {
            message += "Họ tên không được chứa số hoặc ký tự đặc biệt.\n";
            isValid = false;
        }


        // Kiểm tra định dạng dob (yyyy-mm-dd)
        var dobRegex = /^\d{4}-\d{2}-\d{2}$/;
        if (!$('#dob').val().match(dobRegex)) {
            message += "Định dạng ngày sinh phải là yyyy-mm-dd.\n";
            isValid = false;
        }
  
        // Kiểm tra định dạng email
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!$('#email').val().match(emailRegex)) {
            message += "Email chưa đúng định dạng.\n";
            isValid = false;
        }

                try {
            var checkusername =  checkUsernameExistence($('#username').val().toLowerCase());
            if (checkusername === true) {
                message += "user name đã tồn tại.\n";
                isValid = false;
            }
        } catch (error) {
            alert("Lỗi kiểm tra user name: " + error);
            return;
        }


        console.log(formData)


        if (!isValid) {
             alert(message);  // Hiện thông báo lỗi
        }else{

            $.ajax({
                url: 'http://localhost:9999/employee', // Adjust with your real API endpoint
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                headers: {
                    "Authorization": "Bearer " + token   
                },        
                success: function (response) {
                    // Check if the response is successful
                    if (response.status === 200 || response.statusCode === 200) {
                        alert('Employee added successfully!');
                    } else {
                        alert('Failed to add employee: ' + (response.message || 'Unknown error occurred.'));
                    }
                },
                error: function (xhr, status, error) {
                    // Handle error response from the server
                    let errorMessage = xhr.responseJSON ? xhr.responseJSON.message : error;
                    alert('Error: ' + errorMessage);
                    console.error('Error details:', errorMessage); // Log the error
                }
            });
        }
        // Make AJAX request
    });
});


      async function checkUsernameExistence(username) {
    let checkusername = false;
    await $.ajax({
        url: "http://localhost:9999/employee/checkusername/username=" + encodeURIComponent(username),
        method: "POST",
        contentType: "application/x-www-form-urlencoded"
    }).done(function(item){
        checkPhone = item.data;
        console.log("username exists?: " + item);
        console.log(item.data);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log("Error checking username : " + textStatus + ": " + errorThrown);
    });
    return checkusername;
}

  // Đoạn mã tự động thêm dấu gạch ngang vào ngày sinh
  document.getElementById('dob').addEventListener('input', function(e) {
      // Lấy giá trị hiện tại trong trường input
      let input = e.target.value.replace(/\D/g, '');  // Thay đổi này giúp loại bỏ ký tự không phải số

      // Xóa tất cả các dấu gạch ngang nếu có
      input = input.replace(/-/g, '');

      // Kiểm tra nếu độ dài chuỗi đủ để chèn dấu gạch ngang
      if (input.length > 4 && input.length <= 6) {
          // Chèn dấu gạch ngang giữa năm và tháng
          input = input.slice(0, 4) + '-' + input.slice(4);
      } else if (input.length > 6) {
          // Chèn dấu gạch ngang giữa năm, tháng và ngày
          input = input.slice(0, 4) + '-' + input.slice(4, 6) + '-' + input.slice(6);
      }

      // Cập nhật giá trị của trường input với dấu gạch ngang
      e.target.value = input;
      
  });  
