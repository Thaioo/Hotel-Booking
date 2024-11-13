package com.java06.luxurious_hotel.controller;

import com.java06.luxurious_hotel.dto.BookingGuestDTO;
import com.java06.luxurious_hotel.dto.GuestDTO;
import com.java06.luxurious_hotel.request.AddGuestRequest;
import com.java06.luxurious_hotel.request.UpdateGuestRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.BookingService;
import com.java06.luxurious_hotel.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/p{phone}")
    public ResponseEntity<?> getGuestInfoByPhone(@PathVariable(name = "phone") @NotBlank(message = "Please enter your phone!") String phone){

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(userService.getGuestInfoByPhone(phone));

        return new ResponseEntity<>(baseResponse , HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>("Hello user luxurious", HttpStatus.OK);
    }

    @GetMapping("/guests")
    public ResponseEntity<?> getAllGuest(){

        List<GuestDTO> listGuest = userService.getListGuest();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(listGuest);

        return new ResponseEntity<>(baseResponse , HttpStatus.OK);
    }

    @PostMapping("/allbookingguest")
    public ResponseEntity<?> getBookingGuest(@RequestParam int idGuest){
        List<BookingGuestDTO> guestDTOS = bookingService.getListBooking(idGuest);
        BaseResponse response = new BaseResponse();
        response.setData(guestDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/addguest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addGuest(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("summary") String summary,
            @RequestParam("dob") String dob,
            // picture có thể null
            @RequestParam(value = "filePicture", required = false) MultipartFile filePicture) {

        // Tạo đối tượng AddGuestRequest từ các tham số
        AddGuestRequest addGuestRequest = new AddGuestRequest(fullName, phone, email, address, summary, dob, filePicture);
        Boolean check = userService.addGuest(addGuestRequest);
        BaseResponse response = new BaseResponse();
        response.setData(check);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteguest")
    public ResponseEntity<?> deleteGuest(@RequestParam int idGuest){
        BaseResponse response = new BaseResponse();
        response.setData(userService.deleteUser(idGuest));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateguest")
    public ResponseEntity<?> updateGuest(@RequestBody UpdateGuestRequest updateGuestRequest){

        BaseResponse response = new BaseResponse();
        response.setData(userService.updateUser(updateGuestRequest));

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/getvalueuserbyid/{idUser}")
    public ResponseEntity<?> getValueUserById(@PathVariable int idUser){

        BaseResponse response = new BaseResponse();
        response.setData(userService.getUser(idUser));

        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
