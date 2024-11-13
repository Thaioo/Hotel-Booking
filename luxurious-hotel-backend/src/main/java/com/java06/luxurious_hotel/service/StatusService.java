package com.java06.luxurious_hotel.service;

import com.java06.luxurious_hotel.dto.StatusDTO;
import org.springframework.stereotype.Service;

@Service
public interface StatusService {
    StatusDTO getAllStatus();
}
