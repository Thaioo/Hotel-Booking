package com.java06.luxurious_hotel.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddRoomtypeRequest(
        @NotNull(message = "name not null")
        @NotBlank(message = "name not blank")
        String name,

        @NotNull(message = "overview not null")
        @NotBlank(message = "overview not blank")
        String overview,

        @NotNull(message = "price not null")

        double price,

        @NotNull(message = "area not null")

        double area,

        @NotNull(message = "capacity not null")

        int capacity,

        @NotNull(message = "iDBedType not null")

        int iDBedType,

        @NotNull(message = "images not null")

        List<MultipartFile> images,
        @NotNull(message = "idAmenity not null")
        @Valid
        List<@Min(value = 1, message = "idAmenity must be greater than 0")
        @Max(value = 20, message = "idAmenity must be less than or equal to 20")  Integer> idAmenity
        ) {
}
