package com.java06.luxurious_hotel.dto;

import com.java06.luxurious_hotel.dto.coverdto.RoomsDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookingGuestDTO {
    private GuestDTO guestDTO;

    private int idBooking;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String paymentStatus;
    private String paymentMethod;
    private List<RoomsDTO> roomsDTO = new ArrayList<>();
    private int member;
    private int quantilyRoom;
    private double amount;


}
