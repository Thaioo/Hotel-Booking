package com.java06.luxurious_hotel.exception.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //  tạo ra một constructor không tham số
@AllArgsConstructor
public class RoleNotFoundException extends RuntimeException{
    private String message = "Role not found!";
}
