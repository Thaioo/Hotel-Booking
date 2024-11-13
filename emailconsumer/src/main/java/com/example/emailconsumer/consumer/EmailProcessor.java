package com.example.emailconsumer.consumer;

import com.example.emailconsumer.dto.BookingDTO;
import com.example.emailconsumer.service.EmailService;
import com.example.emailconsumer.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailProcessor {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @RabbitListener(queues = "confirm-booking-email-queue")
    public void sendConfirmBookingEmail(String data) throws MessagingException {
        try {
            emailService.sendConfirmBookingEmail(data);
        } catch (Exception e){
            log.error("Error send confirm Email: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "success-booking-email-queue")
    public void sendSuccessBookingEmail(String data) throws MessagingException {
        try {
            emailService.sendSuccessBookingEmail(data);
        } catch (Exception e){
            log.error("Error send successful Email: " + e.getMessage());
        }
    }
}
