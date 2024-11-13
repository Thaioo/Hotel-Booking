package com.example.emailconsumer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomTypeDTO {

    private int id;
    private String name;
    private String overview;
    private double price;
    private double area;
    private int capacity;
    private String bedName;
    private int bedNum;
    private List<String> roomName;
    private List<String> image;
    private String amenity;
    private String amenityNum;

    private List<RoomDTO> rooms;//Trung dùng

}