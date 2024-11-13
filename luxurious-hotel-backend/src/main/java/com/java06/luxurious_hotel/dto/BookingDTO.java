package com.java06.luxurious_hotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    private int id;
    private int idGuest;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private PaymentMethodDTO paymentMethod;
    private PaymentStatusDTO paymentStatus;
    private int adultNo;
    private int childrenNo;
    private HashMap<String, List<String>> roomNo; //Định dạng: RoomType:  101, 102, ...
    private Map<String, List<RoomDTO>> roomTypes;
    private BookingStatusDTO bookingStatus;
    private double paidAmount;
    private double total;
    private List<RoomTypeDTO> roomType;
    private LocalDate createDate;
}
