package com.java06.luxurious_hotel.exception.Employee;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class EmployeeNotExitsException extends RuntimeException {
    private String message;
}
