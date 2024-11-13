package com.java06.luxurious_hotel.controller;

import com.java06.luxurious_hotel.dto.RoomDTO;
import com.java06.luxurious_hotel.dto.searchAvaiRoom.RoomTypeAvailableDTO;
import com.java06.luxurious_hotel.request.AddRoomRequest;
import com.java06.luxurious_hotel.request.SearchRoomRequest;
import com.java06.luxurious_hotel.request.UpdateRoomRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {


    @Autowired
    private RoomService roomService;


    @GetMapping
    public ResponseEntity<?> getAvailableRoom(@Valid SearchRoomRequest searchRoomRequest) {

        List<RoomTypeAvailableDTO> roomVailableDTOList = roomService.getAvailableRooms(searchRoomRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(roomVailableDTOList);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> getAvailableRoomPost(@Valid @RequestBody SearchRoomRequest searchRoomRequest) {

        List<RoomTypeAvailableDTO> roomVailableDTOList = roomService.getAvailableRooms(searchRoomRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(roomVailableDTOList);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/addRoom")
    public ResponseEntity<?> addRoom(@Valid @RequestBody AddRoomRequest addRoomRequest) {


        System.out.println("con meoooooooo");
        roomService.saveRoom(addRoomRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData("New RoomType added successfully");


        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/getRoom/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable int id) {

        RoomDTO roomDTO = roomService.findRoomById(id);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(roomDTO);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateRoom(@Valid @RequestBody UpdateRoomRequest updateRoomRequest) {

        boolean check = roomService.updateRoom(updateRoomRequest);
        BaseResponse baseResponse = new BaseResponse();
        if (check){
            baseResponse.setData("Room updated successfully");
        }else {
            baseResponse.setData("Room update failed");
        }

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id) {

        boolean checkDelete = roomService.deleteRoomById(id);
        BaseResponse baseResponse = new BaseResponse();
        if (checkDelete) {
            baseResponse.setData("Room deleted successfully");
        }else {
            baseResponse.setData("Room not deleted successfully");
        }
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }



    @GetMapping("/allRoom")
    public ResponseEntity<?> getAllRooms() {

        BaseResponse baseResponse = new BaseResponse();
        List<RoomDTO> list = roomService.getAllRoom();

        baseResponse.setData(list);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/{selectDate}")
    public ResponseEntity<?> getAllRoomInfo(@PathVariable String selectDate){
        BaseResponse baseResponse = new BaseResponse();
        var s = roomService.getRoomInfoByDate(selectDate);
        baseResponse.setData(roomService.getRoomInfoByDate(selectDate));
        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }


}
