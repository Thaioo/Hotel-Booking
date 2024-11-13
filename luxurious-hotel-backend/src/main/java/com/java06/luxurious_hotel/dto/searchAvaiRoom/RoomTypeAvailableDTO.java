package com.java06.luxurious_hotel.dto.searchAvaiRoom;

import lombok.Data;

import java.util.List;

@Data
public class RoomTypeAvailableDTO {

    private int id;
    private String roomTypeName;
    private double price;
    private int capacity;
    private  List<String> image;
    private BedTypeDTO bedType;
    private List<RoomAvailableDTO> roomAvailableDTOList;
    private int numberAvailable;


}
