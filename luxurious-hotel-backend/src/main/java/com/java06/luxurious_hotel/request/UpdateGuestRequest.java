package com.java06.luxurious_hotel.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateGuestRequest (
        int idGuest,
        @NotNull(message = "Họ tên không được để trống")
        @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Full name must contain only letters and spaces")
        String fullName,

        @NotNull(message = "phone không được null")
        @Pattern(regexp = "\\d+", message = "Phone number must contain only digits") // quy định đúng định dạng số
        String phone,

        @Email(message = "email chưa đúng định dạng")
        String email,

        // quy định ngày tháng đúng định dạng
        @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Incorrect date format yyyy-MM-dd")
        LocalDate dob,

        String address,

        String summary,
        String image){
}
