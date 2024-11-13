package com.java06.luxurious_hotel.controller;


import com.java06.luxurious_hotel.dto.RoomTypeDTO;
import com.java06.luxurious_hotel.request.AddRoomtypeRequest;
import com.java06.luxurious_hotel.request.UpdateRoomtypeRequest;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.FilesStorageService;
import com.java06.luxurious_hotel.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomType")
//@CrossOrigin(origins = "http://127.0.0.1:5501")  // Allow requests from this origin

public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private FilesStorageService filesStorageService;

    @PostMapping
    public ResponseEntity<?> addRoomType(@Valid AddRoomtypeRequest addRoomtypeRequest){
        roomTypeService.addRoomType(addRoomtypeRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData("New RoomType added successfully");




        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getListRoomType(){

        List<RoomTypeDTO> roomTypeDTOList= roomTypeService.allRoomTypes();

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(roomTypeDTOList);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getRoomTypeDetail(@PathVariable int id){

        RoomTypeDTO roomTypeDTO = roomTypeService.findRoomTypeById(id);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(roomTypeDTO);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable int id){

        BaseResponse baseResponse = new BaseResponse();
        String data = "";
        boolean isSuccess = roomTypeService.deleteRoomTypeById(id);
        if (isSuccess) {

            data ="Delete Roomtype successfully";
        } else {

            data ="Delete Roomtype fail";
        }
        baseResponse.setData(data);



        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> updateRoomType(@Valid UpdateRoomtypeRequest updateRoomtypeRequest){

        BaseResponse baseResponse = new BaseResponse();
        String data = "";
        boolean isSuccess = roomTypeService.updateRoomType(updateRoomtypeRequest);

        System.out.println("isSuccess: " + isSuccess);

        if (isSuccess) {
            data ="Update Roomtype successfully";
        }else {

            data ="Update room type fail, room type not found";

        }

        System.out.println(data);
        baseResponse.setData(data);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getRoomType(@PathVariable String filename){

        Resource resource = filesStorageService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(resource);
    }
}
