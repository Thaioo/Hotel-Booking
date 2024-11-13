package com.java06.luxurious_hotel.dto;

import lombok.Data;

import java.util.List;


@Data
public class RoomVailableDTO {

    private String roomNumber;
    private String roomTypeName;
    private double area;
    private double price;
    private String overview;
    private String bedName;
    private int qltGuest;
    private List<String> image;
    private String amanity;
}
