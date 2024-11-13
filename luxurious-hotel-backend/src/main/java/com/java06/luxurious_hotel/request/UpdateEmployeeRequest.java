package com.java06.luxurious_hotel.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public record UpdateEmployeeRequest (int id,
                                     @NotNull(message = "First Name cannot be null")
                                     @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "name must contain only letters and spaces")
                                     String firstname,
                                     String existingImageName,
                                     @NotNull(message = "Last Name cannot be null")
                                     @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "name must contain only letters and spaces")
                                     String lastname,
                                     @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Incorrect date format yyyy-MM-dd")
                                     String dob,
                                     @NotNull (message = "Phone Number cannot be null")
                                     String phone,
                                     @NotNull (message = "Email  cannot be null")
                                     @Email(message = "Incorrect email format")
                                     String email,
                                     @NotNull (message = "address cannot be null")
                                     String address,
                                     @NotNull (message = "ID Role cannot be null")
                                     int IdRole,
                                     MultipartFile image,
                                     @NotNull (message = "summary cannot be null")
                                     String summary) {
}
