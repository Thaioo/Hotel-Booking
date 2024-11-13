package com.java06.luxurious_hotel.exception.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomNotAvailableException extends RuntimeException{
    private String message;
}
