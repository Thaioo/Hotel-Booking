package com.java06.luxurious_hotel.controller;

import com.java06.luxurious_hotel.request.AuthenRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.AuthenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/authen")
public class AuthenController {

    @Autowired
    private AuthenService authenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenRequest request){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Successful!");
        baseResponse.setData(authenService.login(request));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String request){
        System.out.println("token logged out" + request);
        BaseResponse baseResponse = new BaseResponse();
        authenService.logout(request);
        baseResponse.setMessage("Logged Out!");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
