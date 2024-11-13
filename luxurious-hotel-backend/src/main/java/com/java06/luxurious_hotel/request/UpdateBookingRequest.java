package com.java06.luxurious_hotel.request;

import com.java06.luxurious_hotel.validator.ValidDateRange;
import jakarta.validation.constraints.*;

import java.util.List;

@ValidDateRange(checkInField = "checkInDate", checkOutField = "checkOutDate")
public record UpdateBookingRequest(
        @NotNull(message = "Booking id cannot be null")
        int idBooking,

        @NotNull(message = "idGuest id cannot be null")
        int idGuest,

        @NotNull(message = "checkInDate not null")
        @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Incorrect date format yyyy-MM-dd")
        String checkInDate,

        @NotNull(message = "checkOutDate not null")
        @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Incorrect date format yyyy-MM-dd")
        String checkOutDate,

        int roomNumber,

        @NotNull(message = "Rooms cannot be null")
        @NotEmpty(message = "Rooms cannot be empty")
        List<String> rooms,

        @NotNull(message = "The number of adults cannot be null")
        @Min(value = 1,message = "The number of adults must be at least 1")
        int adultNumber,

        int childrenNumber,

        @Min(value = 1,message = "Booking status id must be at least 1")
        int idBookingStatus,

        @Min(value = 1,message = "Payment status id must be at least 1")
        int idPaymentStatus,

        @Min(value = 1,message = "Payment method id must be at least 1")
        int idPayment,

        @NotNull(message = "paidAmount not null")
        @Min(value = 0,message = "Paid amount must be at least 0")
        double paidAmount,

        @NotNull(message = "total not null")
        @Min(value = 0,message = "Total amount must be at least 0")
        double total
){
}
