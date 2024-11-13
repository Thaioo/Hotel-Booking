package com.java06.luxurious_hotel.exception.authen;

import lombok.Data;

@Data
public class TokenInvalidException extends RuntimeException{
    private String message = "Token is invalid!";
}
