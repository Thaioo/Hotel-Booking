package com.java06.luxurious_hotel.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private int id;
    private String name;
    private String description;
    private List<EmployeeDTO> employees;
}
