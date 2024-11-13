package com.java06.luxurious_hotel.controller;

import com.java06.luxurious_hotel.dto.BookingDTO;
import com.java06.luxurious_hotel.request.AddBookingRequest;
import com.java06.luxurious_hotel.request.ConfirmBookingRequest;
import com.java06.luxurious_hotel.request.UpdateBookingRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.BookingService;
import com.java06.luxurious_hotel.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmBooking(@RequestBody ConfirmBookingRequest request){
        bookingService.confirmBooking(request);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Confirmed booking");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    @GetMapping("/p{phone}")
    public ResponseEntity<?> getBooking(@PathVariable String phone){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(bookingService.getBookingByPhone(phone));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllBooking(){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(bookingService.getAllBooking());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable int id){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(bookingService.getDetailBooking(id));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addBooking(@Valid @RequestBody AddBookingRequest request){
        BookingDTO bookingDTO = bookingService.addNewBooking(request);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("New booking added successfully!");
        baseResponse.setData(bookingDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateBooking(@Valid @RequestBody UpdateBookingRequest request){
        bookingService.updateBooking(request);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Update booking successfully");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable int id){
        bookingService.deleteBooking(id);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Delete booking successfully");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
