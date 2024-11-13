package com.example.emailconsumer.dto;

import java.util.List;

public record AddBookingRequest (
        String firstName,
        String lastName,
        String phone,
        String email,
        String address,
        String checkInDate,
        String checkOutDate,
        int roomNumber,
        List<String> rooms,
        List<String> roomName,
        int adultNumber,
        int childrenNumber,
        int idPaymentStatus,
        int idPayment,
        int idBookingStatus,
        double paidAmount,
        double total,
        int idBooking

){
}