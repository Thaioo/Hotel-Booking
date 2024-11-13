package com.java06.luxurious_hotel.dto;

import com.java06.luxurious_hotel.entity.RoleEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private int id;
    private String firstname;
    private String lastname;
    private LocalDate dob;
    private String phone;
    private String email;
    private String address;
    private RoleDTO role;
    private String image;
    private String sumary;
}
