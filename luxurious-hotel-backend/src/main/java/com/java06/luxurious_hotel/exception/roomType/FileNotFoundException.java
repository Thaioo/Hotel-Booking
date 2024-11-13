package com.java06.luxurious_hotel.exception.roomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileNotFoundException extends RuntimeException {
    private String message = "File not found";
}
