package com.java06.luxurious_hotel.exception.authen;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import lombok.Data;

@Data
public class TokenExpirationException extends RuntimeException {
    private String message = "Token expired or wrong!";
}
