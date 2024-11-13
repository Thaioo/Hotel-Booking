package com.java06.luxurious_hotel.service;

import com.java06.luxurious_hotel.dto.BookingDTO;
import com.java06.luxurious_hotel.entity.BookingEntity;
import com.java06.luxurious_hotel.entity.RoomBookingEntity;
import com.java06.luxurious_hotel.request.AddBookingRequest;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface EmailService {
    String sendEmail(String recipients, String subject, String content, MultipartFile[] files) throws MessagingException;
    String sendConfirmBookingEmail(int idBooking) throws MessagingException;
    String sendSuccessfulBookingEmailToQueue(BookingDTO bookingDTO);
    String sendConfirmBookingEmailToQueue(AddBookingRequest bookingDTO);
    void sendMailResetPassWord(String emailSend,String pass,String to, String subject, String content);
    String resetPassword(String email);
    String keyConfirmResetPass(String email);
    String confirmResetPass(String token,String password);
    String sendConfirmCreateUser(String toEmail) throws MessagingException;
    String ReSendConfirmCreateUser(String toEmail) throws MessagingException;
}
