package com.java06.luxurious_hotel.service;


import com.java06.luxurious_hotel.dto.RoomDTO;
import com.java06.luxurious_hotel.dto.RoomTypeByDateDTO;
import com.java06.luxurious_hotel.dto.RoomTypeDTO;
import com.java06.luxurious_hotel.dto.RoomVailableDTO;
import com.java06.luxurious_hotel.dto.searchAvaiRoom.RoomTypeAvailableDTO;
import com.java06.luxurious_hotel.request.AddRoomRequest;
import com.java06.luxurious_hotel.request.SearchRoomRequest;
import com.java06.luxurious_hotel.request.UpdateRoomRequest;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public interface RoomService {
    List<RoomTypeByDateDTO> getRoomInfoByDate(String chooseDate);
    List<RoomTypeAvailableDTO> getAvailableRooms(SearchRoomRequest searchRoomRequest);
    void saveRoom(AddRoomRequest addRoomRequest);
    RoomDTO findRoomById(int id);
    boolean deleteRoomById(int id);
    boolean updateRoom(UpdateRoomRequest updateRoomRequest);
    List<RoomDTO> getAllRoom();
}
