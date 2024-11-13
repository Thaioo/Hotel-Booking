package com.java06.luxurious_hotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuestByDateDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private int idSelectBooking;
    private List<String> otherRooms;
}
