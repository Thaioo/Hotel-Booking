package com.java06.luxurious_hotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuestDTO {
    private int id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String summary;
    private LocalDate dob;
    private String firstName;
    private String lastName;
    private String linkImage;
}
