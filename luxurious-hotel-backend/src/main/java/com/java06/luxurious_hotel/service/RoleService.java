package com.java06.luxurious_hotel.service;


import com.java06.luxurious_hotel.dto.RoleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<RoleDTO> getRoles();
    List<RoleDTO> getAllRoles();
}
