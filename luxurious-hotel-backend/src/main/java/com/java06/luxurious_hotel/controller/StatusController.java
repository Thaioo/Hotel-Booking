package com.java06.luxurious_hotel.controller;

import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping
    public ResponseEntity<?> getAllStatus(){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(statusService.getAllStatus());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
