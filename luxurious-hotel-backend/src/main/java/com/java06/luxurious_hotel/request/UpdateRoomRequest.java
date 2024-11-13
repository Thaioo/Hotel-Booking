package com.java06.luxurious_hotel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRoomRequest(
        @NotNull(message = "id Room not null")
        int id,
        @NotNull(message = "name not null")
        @NotBlank(message = "name not blank")
        String name,

        @NotNull(message = "id Room Type not null")
        int idRoomType)  {

}
