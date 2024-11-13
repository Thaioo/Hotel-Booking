package com.java06.luxurious_hotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomByDateDTO {
    private int id;
    private String name;
    private GuestByDateDTO guestCheckIn;
    private GuestByDateDTO guestCheckOut;
    private GuestByDateDTO guestStaying;
    private List<String> otherRooms;
}
