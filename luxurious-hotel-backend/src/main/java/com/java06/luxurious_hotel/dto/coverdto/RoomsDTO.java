package com.java06.luxurious_hotel.dto.coverdto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomsDTO {
    private String nameRoomType;
    private List<String> roomNumber = new ArrayList<>();
}
