package com.java06.luxurious_hotel.request;

import com.java06.luxurious_hotel.validator.ValidDateRange;
import jakarta.validation.constraints.*;

import java.util.List;

@ValidDateRange(checkInField = "checkInDate", checkOutField = "checkOutDate")
public record AddBookingRequest (
        @NotNull(message = "First name not null")
        @NotBlank(message = "First name not blank")
        String firstName,

        @NotNull(message = "Last name not null")
        @NotBlank(message = "Last name not blank")
        String lastName,

        @NotNull(message = "Phone not null")
        @NotBlank(message = "Phone not blank")
        @Pattern(regexp = "\\d+", message = "Phone number must contain only digits") // quy định đúng định dạng số
        String phone,

        @Email(message = "Incorrect email format")
        String email,

        String address,

        @NotNull(message = "checkInDate not null")
        @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Check in: Incorrect date format yyyy-MM-dd")
        String checkInDate,

        @NotNull(message = "checkOutDate not null")
        @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Check out: Incorrect date format yyyy-MM-dd")
        String checkOutDate,

        int roomNumber,

        @NotNull(message = "Rooms not null!")
        @NotEmpty(message = "rooms not empty!")
        List<String> rooms,

        List<String> roomName,

        @NotNull(message = "adultNumber not null")
        @Min(value = 1,message = "The number of adults must be at least 1")
        int adultNumber,

        @NotNull(message = "childrenNumber not null")
        int childrenNumber,

        int idPaymentStatus,

        @NotNull(message = "idPayment not null")
        int idPayment,

        int idBookingStatus,

        double paidAmount,

        @NotNull(message = "total not null")
        double total,

        int idBooking

        ){
}
