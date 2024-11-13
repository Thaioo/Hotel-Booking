package com.java06.luxurious_hotel.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest( @NotNull(message = "User Name cannot be null")
                            String username,
                             @NotNull(message = "User Name cannot be null")
                             @Email(message = "Incorrect email format")
                            String email,
                            String password,
                            String phone,
                            @NotNull(message = "First Name cannot be null")
                            @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = " name must contain only letters and spaces")
                            String firstname,
                            @NotNull(message = "last Name cannot be null")
                            @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = " name must contain only letters and spaces")
                            String lastname) {
}
