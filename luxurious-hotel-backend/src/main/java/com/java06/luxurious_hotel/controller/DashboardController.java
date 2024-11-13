package com.java06.luxurious_hotel.controller;

import com.java06.luxurious_hotel.dto.BookingDTO;
import com.java06.luxurious_hotel.entity.BookingEntity;
import com.java06.luxurious_hotel.repository.BookingRepository;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboard(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate){


//        // Tính số ngày trong chu kỳ hiện tại
//        long daysInCycle = startDate.until(endDate).getDays() + 1;
//
//        // Tính chu kỳ trước đó
//        LocalDate previousEndDate = startDate.minusDays(1); // Ngày kết thúc chu kỳ trước đó
//        LocalDate previousStartDate = previousEndDate.minusDays(daysInCycle - 1); // Ngày bắt đầu chu kỳ trước đó
//
//        List<BookingEntity> bookings =
//                bookingRepository.findByCheckOutBetween(startDate.atStartOfDay(), endDate.atStartOfDay());
//
//        List<BookingEntity> previousBookings =
//                bookingRepository.findByCheckOutBetween(previousStartDate.atStartOfDay(), previousEndDate.atStartOfDay());
//
//        BaseResponse baseResponse = new BaseResponse();
//
//        List<BookingDTO> listBookingDTO = bookings.stream().map(bookingEntity -> {
//            BookingDTO bookingDTO = new BookingDTO();
//            bookingDTO.setId(bookingEntity.getId());
//            bookingDTO.setCheckIn(bookingEntity.getCheckIn().toLocalDate());
//            bookingDTO.setCheckOut(bookingEntity.getCheckOut().toLocalDate());
//
//            return bookingDTO;
//        }).toList();
//
//        List<BookingDTO> listPreviousBookingDTO = previousBookings.stream().map(bookingEntity -> {
//            BookingDTO bookingDTO = new BookingDTO();
//            bookingDTO.setId(bookingEntity.getId());
//            bookingDTO.setCheckIn(bookingEntity.getCheckIn().toLocalDate());
//            bookingDTO.setCheckOut(bookingEntity.getCheckOut().toLocalDate());
//
//            return bookingDTO;
//        }).toList();
//        HashMap<String, List> hashMap = new HashMap<>();
//        hashMap.put("listPreviouseBooking", listPreviousBookingDTO);
//        hashMap.put("listBooking", listBookingDTO);


        return new ResponseEntity<>(dashboardService.getDashboard(startDate, endDate), HttpStatus.OK);
//        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }
}
