package com.java06.luxurious_hotel.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.dto.*;
import com.java06.luxurious_hotel.entity.RoomEntity;
import com.java06.luxurious_hotel.repository.BookingStatusRepository;
import com.java06.luxurious_hotel.repository.PaymentMethodRepository;
import com.java06.luxurious_hotel.repository.PaymentStatusRepository;
import com.java06.luxurious_hotel.repository.RoomRepository;
import com.java06.luxurious_hotel.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatusServiceImp implements StatusService {

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private BookingStatusRepository bookingStatusRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public StatusDTO getAllStatus() {
        StatusDTO statusDTO = new StatusDTO();

        //Nếu dữ liệu có trong redis
        if (redisTemplate.hasKey("statusDTO")){
            System.out.println("Có redis");
            var json = redisTemplate.opsForValue().get("statusDTO").toString();
            try {
                statusDTO = objectMapper.readValue(json, StatusDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Error convert JSON to statusDTO: " + e);
            }
            return statusDTO;
        }
        System.out.println("Không redis");


        statusDTO.setListBookingStatus(bookingStatusRepository.findAll().stream().map(bookingStatus ->
                new BookingStatusDTO(bookingStatus.getId(), bookingStatus.getName())).toList());

        statusDTO.setListPaymentStatus(paymentStatusRepository.findAll().stream().map(paymentStatus ->
                new PaymentStatusDTO(paymentStatus.getId(), paymentStatus.getName())).toList());

        statusDTO.setListPaymentMethod(paymentMethodRepository.findAll().stream().map(paymentMethod ->
                new PaymentMethodDTO(paymentMethod.getId(), paymentMethod.getName())).toList());

//        Map<String, List<RoomDTO>> roomTypeMap;
//        roomTypeMap = roomRepository.findAll().stream().collect(Collectors.groupingBy(
//                roomEntity -> roomEntity.getRoomType().getName(),
//                Collectors.mapping(roomEntity -> {
//                    RoomDTO roomDTO = new RoomDTO();
//                    roomDTO.setId(roomEntity.getId());
//                    roomDTO.setName(roomEntity.getName());
//                    return roomDTO;
//                }, Collectors.toList())
//        ));
//        statusDTO.setListRoomType(roomTypeMap);
        List<RoomEntity> rooms = roomRepository.findAll();
        Map<String, RoomTypeDTO> roomTypeMap = new LinkedHashMap<>();

        for (RoomEntity room : rooms) {
            String roomTypeName = room.getRoomType().getName();

            //Mục đích: chỉ Khởi tạo RoomTypeAvailableDTO 1 lần thôi
            if (!roomTypeMap.containsKey(roomTypeName)) {
                RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
                roomTypeDTO.setId(room.getRoomType().getId());
                roomTypeDTO.setName(room.getRoomType().getName());
                roomTypeDTO.setPrice(room.getRoomType().getPrice());
                roomTypeDTO.setCapacity(room.getRoomType().getCapacity());
                roomTypeDTO.setRooms(new ArrayList<>());

                roomTypeMap.put(roomTypeName, roomTypeDTO);
            }

            // Add available room to the corresponding room type
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(room.getId());
            roomDTO.setName(room.getName());

            roomTypeMap.get(roomTypeName).getRooms().add(roomDTO);
        }

        statusDTO.setListRoomType(new ArrayList<>(roomTypeMap.values()));

        try{
            String json = objectMapper.writeValueAsString(statusDTO);
            redisTemplate.opsForValue().set("statusDTO", json, Duration.ofDays(7));
        } catch (Exception e){
            throw new RuntimeException("Error parse statusDTO to JSON: " + e);
        }

        return statusDTO;
    }
}