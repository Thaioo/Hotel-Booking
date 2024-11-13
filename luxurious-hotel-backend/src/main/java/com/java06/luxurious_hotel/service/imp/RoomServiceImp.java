package com.java06.luxurious_hotel.service.imp;

import com.java06.luxurious_hotel.dto.*;
import com.java06.luxurious_hotel.dto.searchAvaiRoom.BedTypeDTO;
import com.java06.luxurious_hotel.dto.searchAvaiRoom.RoomAvailableDTO;
import com.java06.luxurious_hotel.dto.searchAvaiRoom.RoomTypeAvailableDTO;
import com.java06.luxurious_hotel.entity.*;
import com.java06.luxurious_hotel.exception.room.RoomNotFoundException;
import com.java06.luxurious_hotel.repository.*;
import com.java06.luxurious_hotel.request.AddRoomRequest;
import com.java06.luxurious_hotel.request.SearchRoomRequest;
import com.java06.luxurious_hotel.request.UpdateRoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImp implements com.java06.luxurious_hotel.service.RoomService {


    @Value("${key.list.roomtype}")
    private String listRoomTypeKey;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoomTypeRepository roomTypeRepository;



    @Autowired
    private RoomBookingRepository roomBookingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomAmenityRepository roomAmenityRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<RoomTypeByDateDTO> getRoomInfoByDate(String selectDateString) {
        LocalDateTime selectDate = LocalDate.parse(selectDateString).atStartOfDay();

        //Những booking có trong select date
        List<RoomBookingEntity> roomBookingEntityList = roomBookingRepository.findRoomBookingsBySelectedDate(selectDate);

        //Lấy tất cả các Room Type của khách sạn
        return roomTypeRepository.findAll().stream().map(roomTypeEntity -> {

            RoomTypeByDateDTO roomTypeByDateDTO = new RoomTypeByDateDTO();
            roomTypeByDateDTO.setId(roomTypeEntity.getId());
            roomTypeByDateDTO.setName(roomTypeEntity.getName());

            //Lấy tất cả các Room của khách sạn
            roomTypeByDateDTO.setRooms(roomTypeEntity.getRooms().stream().map(roomEntity -> {
                RoomByDateDTO roomByDateDTO = new RoomByDateDTO();
                roomByDateDTO.setId(roomEntity.getId());
                roomByDateDTO.setName(roomEntity.getName());

                //So sánh tất cả Room của khách sạn với roomBooking để lấy thông tin khách hàng
                roomBookingEntityList.forEach(roomBookingEntity -> {

                    if (roomBookingEntity.getRoom().getId() == roomEntity.getId()){

                        //Guest checkin
                        if (roomBookingEntity.getBooking().getCheckIn().equals(selectDate)) {
                            roomByDateDTO.setGuestCheckIn(this.getGuestByDateDTO(roomBookingEntity));
                        }

                        //Guest checkout
                        if (roomBookingEntity.getBooking().getCheckOut().equals(selectDate)) {
                            roomByDateDTO.setGuestCheckOut(this.getGuestByDateDTO(roomBookingEntity));
                        }

                        //Guest Staying
                        if (roomBookingEntity.getBooking().getBookingStatus().getId() == 3) { //Trạng thái Checked In
                            GuestByDateDTO guestByDateDTO = this.getGuestByDateDTO(roomBookingEntity);

                            List<String> otherRooms = new ArrayList<>();
                            roomBookingEntityList.forEach(roomBookingEntity1 -> {
                                if (roomBookingEntity.getBooking().equals(roomBookingEntity1.getBooking())
                                    && !roomBookingEntity.getRoom().equals(roomBookingEntity1.getRoom())){
                                    otherRooms.add(roomBookingEntity1.getRoom().getName());
                                }
                            });
                            guestByDateDTO.setOtherRooms(otherRooms);
                            roomByDateDTO.setGuestStaying(guestByDateDTO);
                        }
                    }
                });

                return roomByDateDTO;
            }).toList());

            return roomTypeByDateDTO;
        }).toList();
    }

    private GuestByDateDTO getGuestByDateDTO(RoomBookingEntity roomBookingEntity) {
        GuestByDateDTO guestDTO = new GuestByDateDTO();
        guestDTO.setIdSelectBooking(roomBookingEntity.getBooking().getId());
        guestDTO.setId(roomBookingEntity.getBooking().getGuest().getId());
        guestDTO.setPhone(roomBookingEntity.getBooking().getGuest().getPhone());
        guestDTO.setFirstName(roomBookingEntity.getBooking().getGuest().getFirstName());
        guestDTO.setLastName(roomBookingEntity.getBooking().getGuest().getLastName());
        return guestDTO;
    }

    public LocalDateTime parseDate(String date) {
        String data = date.trim() + "T00:00:00";
        return LocalDateTime.parse(data);

    }

    @Transactional
    @Override
    public List<RoomTypeAvailableDTO> getAvailableRooms(SearchRoomRequest searchRoomRequest) {
        List<RoomAvailableInfo> roomAvailableInfoList = roomRepository.findAvailableRoom(
                parseDate(searchRoomRequest.checkIn()), parseDate(searchRoomRequest.checkOut()));


        // sắp xếp theo tên roomType
        roomAvailableInfoList.sort(Comparator.comparing(roomAvailableInfo -> roomAvailableInfo.getRoomEntity().getRoomType().getName()));

        // tạo 1 map để để chứ các room type:
        Map<String, RoomTypeAvailableDTO> roomTypeMap = new HashMap<>();

        int num =1;
        boolean checkIn = false;
        for (RoomAvailableInfo roomAvailableInfo : roomAvailableInfoList) {
            String roomTypeName = roomAvailableInfo.getRoomEntity().getRoomType().getName();


            //Mục đích: chỉ Khởi tạo RoomTypeAvailableDTO 1 lần thôi
            if (!roomTypeMap.containsKey(roomTypeName)) {
                checkIn = true;

                RoomTypeAvailableDTO roomTypeAvailableDTO = new RoomTypeAvailableDTO();
                roomTypeAvailableDTO.setId(roomAvailableInfo.getRoomEntity().getRoomType().getId());
                roomTypeAvailableDTO.setRoomTypeName(roomTypeName);
                roomTypeAvailableDTO.setPrice(roomAvailableInfo.getRoomEntity().getRoomType().getPrice());
                roomTypeAvailableDTO.setCapacity(roomAvailableInfo.getRoomEntity().getRoomType().getCapacity());

                String imagesRoomType = roomAvailableInfo.getRoomEntity().getRoomType().getImage();
                if (imagesRoomType != null && !imagesRoomType.isEmpty()) {
                    List<String> images = Arrays.stream(imagesRoomType.split(","))//
                            .collect(Collectors.toList()).stream()
                            .map(item -> ("http://localhost:9999/roomType/file/"+item)).toList();
                    roomTypeAvailableDTO.setImage(images);
                }

                roomTypeAvailableDTO.setRoomAvailableDTOList(new ArrayList<>());

                // Create BedTypeDTO
                BedTypeDTO bedTypeDTO = new BedTypeDTO();
                bedTypeDTO.setId(roomAvailableInfo.getRoomEntity().getRoomType().getBedType().getId());
                bedTypeDTO.setName(roomAvailableInfo.getRoomEntity().getRoomType().getBedType().getName());
                roomTypeAvailableDTO.setBedType(bedTypeDTO);


                roomTypeMap.put(roomTypeName, roomTypeAvailableDTO);

            }
            if (checkIn==false){
                num++;
                roomTypeMap.get(roomTypeName).setNumberAvailable(num);
            }
            if (checkIn==true){

                num = 1;
                roomTypeMap.get(roomTypeName).setNumberAvailable(num);
                checkIn = false;
            }

            //System.out.println("number is "+ num);

            // Add available room to the corresponding room type
            RoomAvailableDTO roomAvailableDTO = new RoomAvailableDTO();
            roomAvailableDTO.setRoomId(roomAvailableInfo.getRoomEntity().getId());
            roomAvailableDTO.setRoomName(roomAvailableInfo.getRoomEntity().getName());

            roomTypeMap.get(roomTypeName).getRoomAvailableDTOList().add(roomAvailableDTO);
        }


        return new ArrayList<>(roomTypeMap.values());
    }
    @Transactional
    @Override
    public void saveRoom(AddRoomRequest addRoomRequest) {

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setName(addRoomRequest.name());

        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
        roomTypeEntity.setId(addRoomRequest.idRoomType());
        roomEntity.setRoomType(roomTypeEntity);

        roomRepository.save(roomEntity);
        redisTemplate.delete(listRoomTypeKey);
    }

    @Override
    public RoomDTO findRoomById(int id) {
         RoomEntity  roomEntity = roomRepository.findById(id).orElseThrow(()-> new RuntimeException("Room not found"));


            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(roomEntity.getId());
            roomDTO.setName(roomEntity.getName());
            RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
            roomTypeDTO.setId(roomEntity.getRoomType().getId());
            roomTypeDTO.setName(roomEntity.getRoomType().getName());
            roomDTO.setRoomTypeDTO(roomTypeDTO);


        return roomDTO;
    }

    @Transactional
    @Override
    public boolean deleteRoomById(int id) {
        if (!roomRepository.existsById(id)){
            throw new RoomNotFoundException("Room with id: "+ id +" not found!!!");
        }




        if (roomRepository.existsById(id)) {
            if (roomBookingRepository.existsByRoomId(id)) {
                roomBookingRepository.deleteAllByRoomId(id);
            }
            roomRepository.deleteById(id);

            redisTemplate.delete(listRoomTypeKey);
            return true;

        }
        return false;
    }

    @Override
    public boolean updateRoom(UpdateRoomRequest updateRoomRequest) {
        boolean checkIn = false;
        if (!roomRepository.existsById(updateRoomRequest.id())) {


            throw new RoomNotFoundException("Room Type with id: "+ updateRoomRequest.id() +" not found!!!");
        }
        if (!roomTypeRepository.existsById(updateRoomRequest.idRoomType())) {

            throw new RoomNotFoundException("Room Type with id: "+ updateRoomRequest.idRoomType() +" not found!!!");
        }

        if (roomRepository.existsById(updateRoomRequest.id()) && roomTypeRepository.existsById(updateRoomRequest.idRoomType())) {
            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setId(updateRoomRequest.id());
            roomEntity.setName(updateRoomRequest.name());

            RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
            roomTypeEntity.setId(updateRoomRequest.idRoomType());
            roomEntity.setRoomType(roomTypeEntity);
            roomRepository.save(roomEntity);
            checkIn = true;
            // xoá redis sau mỗi lần chỉnh sữa
            redisTemplate.delete(listRoomTypeKey);
        }

        return checkIn;
    }

    @Override
    public List<RoomDTO> getAllRoom() {
        List<RoomEntity> roomEntityList = roomRepository.findAll();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (RoomEntity roomEntity : roomEntityList) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(roomEntity.getId());
            roomDTO.setName(roomEntity.getName());
            RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
            roomTypeDTO.setId(roomEntity.getRoomType().getId());
            roomTypeDTO.setName(roomEntity.getRoomType().getName());
            roomDTO.setRoomTypeDTO(roomTypeDTO);
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }


}


