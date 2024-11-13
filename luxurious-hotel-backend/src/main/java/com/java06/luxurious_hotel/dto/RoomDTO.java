package com.java06.luxurious_hotel.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private int id;
    private String name;
    private RoomTypeDTO roomTypeDTO;
}
