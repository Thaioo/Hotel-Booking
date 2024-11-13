package com.java06.luxurious_hotel.controller;


import com.java06.luxurious_hotel.request.ConfirmCreateAccount;
import com.java06.luxurious_hotel.request.SignUpRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.SignUpService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @PostMapping
    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) throws MessagingException {
        signUpService.signUp(signUpRequest);
        BaseResponse response = new BaseResponse();
        response.setMessage("Sign up successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public void VerifysignUp(ConfirmCreateAccount account, HttpServletResponse resp) throws MessagingException , IOException{
        String status= signUpService.verifySignUp(account);
        if (status.equals("NOT OK")){
            String url= "http://127.0.0.1:5501/admin/UserExits.html";
            resp.sendRedirect(url);
        }else if (status.equals("OK")){
            String url= "http://127.0.0.1:5501/admin/CreateUserSuccess.html";
            resp.sendRedirect(url);
        }else {
            String url="http://127.0.0.1:5501/admin/CreateUserFalse.html";
            resp.sendRedirect(url);
        }
    }
}
