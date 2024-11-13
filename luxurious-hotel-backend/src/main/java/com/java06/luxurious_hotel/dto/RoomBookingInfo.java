package com.java06.luxurious_hotel.dto;

import com.java06.luxurious_hotel.entity.RoomEntity;
import lombok.Data;

@Data
public class RoomBookingInfo {

    private RoomEntity room;
    private String userName;

    public RoomBookingInfo(RoomEntity room, String userName) {
        this.room = room;
        this.userName = userName;
    }
}
