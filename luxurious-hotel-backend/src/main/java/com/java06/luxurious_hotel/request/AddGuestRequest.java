package com.java06.luxurious_hotel.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public record AddGuestRequest(
        @NotNull(message = "Họ tên không được để trống")
        @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Full name must contain only letters and spaces")
        String fullName,

        @NotNull(message = "phone không được null")
        @Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
        String phone,

        @Email(message = "email chưa đúng định dạng")
        String email,

        String address,

        String summary,

        @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Incorrect date format yyyy-MM-dd")
        String dob,
        @Nullable
        MultipartFile filePicture) {
}
