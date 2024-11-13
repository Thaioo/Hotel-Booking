package com.java06.luxurious_hotel.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public record AddEmployeeRequest(
                                @NotNull(message = "First Name cannot be null")
                                 @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = " name must contain only letters and spaces")
                                 String firstName,

                                 String username,
                                 String password,
                                 int Idrole,
                                 @NotNull(message = "last Name cannot be null")
                                @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = " name must contain only letters and spaces")
                                 String lastName,
                                 String dob,
                                 String phone,
                                @Email(message = "Incorrect email format")
                                 String email,
                                 String address,
                                 String summary,
                                 @NotNull (message = "image cannot be null")
                                 MultipartFile image) {
}
