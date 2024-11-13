package com.java06.luxurious_hotel.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomTypeByDateDTO {
    private int id;
    private String name;
    private List<RoomByDateDTO> rooms;
}
