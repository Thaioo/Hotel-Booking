package com.java06.luxurious_hotel.dto;

import lombok.Data;

@Data
public class AuthorityDTO {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private String image;
}
