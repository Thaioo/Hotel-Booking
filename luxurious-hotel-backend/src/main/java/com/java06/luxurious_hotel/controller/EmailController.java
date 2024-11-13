package com.java06.luxurious_hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.config.RabbitmqConfig;
import com.java06.luxurious_hotel.request.AddBookingRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.EmailService;
import com.java06.luxurious_hotel.service.SignUpService;
import com.java06.luxurious_hotel.service.imp.EmailServiceImp;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailServiceImp emailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestParam String recipients, @RequestParam String subject,
                                       @RequestParam String content, @RequestParam(required = false) MultipartFile[] files) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(emailService.sendEmail(recipients, subject, content, files));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    //Controller này là gửi email confirm bằng chính máy chủ
    @PostMapping("/confirmation")
    public ResponseEntity<?> sendEmail(@RequestParam int idBooking) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(emailService.sendConfirmBookingEmail(idBooking));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    //Gửi thông tin booking cần confirm lên RabbitMQ để xử lý gửi email confirm
    @PostMapping("/sendBookingInfoToQueue")
    public ResponseEntity<?> sendToQueue(@RequestBody AddBookingRequest request){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(emailService.sendConfirmBookingEmailToQueue(request));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email){
        System.out.println(email);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(emailService.resetPassword(email));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/confirmpassword/{email}")
    public void linkConfirmResetPass(@PathVariable String email, HttpServletResponse response) throws IOException {
        String verificationKey = emailService.keyConfirmResetPass(email);

        // Chuyển hướng tới trang reset password với mã xác nhận trong query parameter
        String redirectUrl = "http://127.0.0.1:5501/admin/resetpassword.html?key=" + verificationKey;
        response.sendRedirect(redirectUrl);
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(@RequestParam String token,
                                            @RequestParam String password){

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(emailService.confirmResetPass(token,password));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }



    @GetMapping("/resend")
    public  ResponseEntity<?> resendEmail(String email) throws MessagingException {
        emailService.ReSendConfirmCreateUser(email);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("send email success");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
