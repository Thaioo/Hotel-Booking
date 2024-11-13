package com.java06.luxurious_hotel.dto;

import com.java06.luxurious_hotel.entity.RoomEntity;
import com.java06.luxurious_hotel.entity.RoomTypeEntity;
import lombok.Data;

@Data
public class RoomAvailableInfo {

     RoomEntity roomEntity;
     RoomTypeEntity roomTypeEntity;

    public RoomAvailableInfo(RoomEntity roomEntity, RoomTypeEntity roomTypeEntity) {
        this.roomEntity = roomEntity;
        this.roomTypeEntity = roomTypeEntity;

    }

    public RoomAvailableInfo(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public RoomAvailableInfo(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }
}
