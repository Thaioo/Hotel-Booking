package com.java06.luxurious_hotel.service;


import com.java06.luxurious_hotel.dto.BookingGuestDTO;

import com.java06.luxurious_hotel.dto.BookingDTO;

import com.java06.luxurious_hotel.entity.BookingEntity;
import com.java06.luxurious_hotel.request.AddBookingRequest;
import com.java06.luxurious_hotel.request.ConfirmBookingRequest;
import com.java06.luxurious_hotel.request.UpdateBookingRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<BookingDTO> getBookingByPhone(String phone);
    List<BookingDTO> getAllBooking();
    BookingDTO getDetailBooking(int idBooking);
    BookingDTO addNewBooking(AddBookingRequest request);
    void confirmBooking(ConfirmBookingRequest request);
    List<BookingGuestDTO> getListBooking(int idGuest);
    void updateBooking(UpdateBookingRequest request);
    void deleteBooking(int idBooking);

}
