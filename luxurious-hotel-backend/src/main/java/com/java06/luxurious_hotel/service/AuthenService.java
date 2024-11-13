package com.java06.luxurious_hotel.service;

import com.java06.luxurious_hotel.request.AuthenRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenService {
    String login(AuthenRequest request);
    boolean logout(String token);
}
