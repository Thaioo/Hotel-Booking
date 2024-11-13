package com.java06.luxurious_hotel.service;


import com.java06.luxurious_hotel.request.ConfirmCreateAccount;
import com.java06.luxurious_hotel.request.SignUpRequest;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface SignUpService {
    void signUp(SignUpRequest signUpRequest) throws MessagingException;
    String verifySignUp(ConfirmCreateAccount account) throws MessagingException;
}
