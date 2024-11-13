package com.java06.luxurious_hotel.service.imp;

import com.java06.luxurious_hotel.dto.DashboardDTO;
import com.java06.luxurious_hotel.entity.BookingEntity;
import com.java06.luxurious_hotel.repository.BookingRepository;
import com.java06.luxurious_hotel.repository.RoomRepository;
import com.java06.luxurious_hotel.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImp implements DashboardService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public DashboardDTO getDashboard(LocalDate startDate, LocalDate endDate) {

        DashboardDTO dashboardDTO = new DashboardDTO();

        // Tính số ngày trong chu kỳ hiện tại
        int daysInCycle = startDate.until(endDate).getDays() + 1;

        // Tính chu kỳ trước đó
        LocalDate previousEndDate = startDate.minusDays(1); // Ngày kết thúc chu kỳ trước đó
        LocalDate previousStartDate = previousEndDate.minusDays(daysInCycle - 1); // Ngày bắt đầu chu kỳ trước đó

        // Tìm tất cả booking trong khoảng thời gian từ previousStartDate đến endDate
        List<BookingEntity> allBookings = bookingRepository.findByCheckOutBetween(
                previousStartDate.atStartOfDay(), endDate.atStartOfDay());

        // Phân loại booking cho từng chu kỳ
        List<BookingEntity> currentBookings = allBookings.stream()
                .filter(booking ->
                        !booking.getCheckOut().isBefore(startDate.atStartOfDay()) &&
                                !booking.getCheckOut().isAfter(endDate.atStartOfDay()))
                .collect(Collectors.toList());

        List<BookingEntity> previousBookings = allBookings.stream()
                .filter(booking ->
                        !booking.getCheckOut().isBefore(previousStartDate.atStartOfDay()) &&
                                !booking.getCheckOut().isAfter(previousEndDate.atStartOfDay()))
                .collect(Collectors.toList());

        //TÍNH TỔNG SỐ KHÁCH GHÉ THĂM VÀ PHẦN TRĂM TĂNG TRƯỞNG SO VỚI CHU KỲ TRƯỚC
        int totalPeopleCurrentCycle = currentBookings.stream().map(BookingEntity::getAdultNumber)
                .reduce(0, Integer::sum);

        int totalPeoplePreviousCycle = previousBookings.stream().map(BookingEntity::getAdultNumber)
                .reduce(0, Integer::sum);

        int growthVisitor = this.calculateGrowth(totalPeopleCurrentCycle, totalPeoplePreviousCycle);

        dashboardDTO.setTotalVisitor(totalPeopleCurrentCycle);
        dashboardDTO.setPreVisitor(totalPeoplePreviousCycle);
        dashboardDTO.setGrowthVisitor(growthVisitor);

        //TÍNH TỔNG SỐ BOOKING VÀ PHẦN TRĂM TĂNG TRƯỞNG SO VỚI CHU KỲ TRƯỚC
        int totalCurrentBooking = currentBookings.size();
        int totalPreviousBooking = previousBookings.size();
        int growthBooking = this.calculateGrowth(totalCurrentBooking, totalPreviousBooking);

        dashboardDTO.setTotalBooking(totalCurrentBooking);
        dashboardDTO.setPreBooking(totalPreviousBooking);
        dashboardDTO.setGrowthBooking(growthBooking);

        //TÍNH TỔNG SỐ DOANH THU VÀ PHẦN TRĂM TĂNG TRƯỞNG SO VỚI CHU KỲ TRƯỚC
        double totalRevenueCurrentCycle = currentBookings.stream().map(BookingEntity::getTotal)
                .reduce(0d, Double::sum);
        double totalRevenuePreviousCycle = previousBookings.stream().map(BookingEntity::getTotal)
                .reduce(0d, Double::sum);

        int growthRevenue = this.calculateGrowth((int)totalRevenueCurrentCycle, (int)totalRevenuePreviousCycle);

        dashboardDTO.setTotalRevenue(totalRevenueCurrentCycle);
        dashboardDTO.setPreRevenue(totalRevenuePreviousCycle);
        dashboardDTO.setGrowthRevenue(growthRevenue);


        //Tính tỷ lệ lấp đầy và phần trăm tăng trưởng so với chu kỳ trước
        int totalRoom = roomRepository.findAll().size();

        List<BookingEntity> allBooked = bookingRepository.findByCheckOutGreaterThanEqualAndCheckInLessThanEqual(
                previousStartDate.atStartOfDay(), endDate.atStartOfDay());

        // Phân loại booking cho từng chu kỳ
        List<BookingEntity> bookedBookingCurrentCycle = allBookings.stream()
                .filter(booking ->
                        !booking.getCheckOut().isBefore(startDate.atStartOfDay()) &&
                                !booking.getCheckIn().isAfter(endDate.atStartOfDay()))
                .collect(Collectors.toList());

        List<BookingEntity> bookedBookingPreviousCycle = allBookings.stream()
                .filter(booking ->
                        !booking.getCheckOut().isBefore(previousStartDate.atStartOfDay()) &&
                                !booking.getCheckIn().isAfter(previousEndDate.atStartOfDay()))
                .collect(Collectors.toList());

        int totalBookedDaysCurrentCycle  = bookedBookingCurrentCycle.stream().mapToInt(booking -> this.calculateOverlapDays(booking, startDate, endDate)).sum();
        int totalBookedDaysPreviousCycle  = bookedBookingPreviousCycle.stream().mapToInt(booking -> this.calculateOverlapDays(booking, previousStartDate, previousEndDate)).sum();

        int occupancyRate = totalBookedDaysCurrentCycle * 100 / (totalRoom * daysInCycle) ;
        int previousOccupancyRate = totalBookedDaysPreviousCycle * 100 / (totalRoom * daysInCycle) ;

        int growthOccupancy = this.calculateGrowth(occupancyRate, previousOccupancyRate);

        dashboardDTO.setOccupancyRate(occupancyRate);
        dashboardDTO.setPreviousOccupancyRate(previousOccupancyRate);
        dashboardDTO.setGrowthOccupancy(growthOccupancy);

        return dashboardDTO;
    }

    private int calculateOverlapDays(BookingEntity booking, LocalDate startDate, LocalDate endDate) {
        LocalDate bookingStart = booking.getCheckIn().toLocalDate().isAfter(startDate) ? booking.getCheckIn().toLocalDate() : startDate;
        LocalDate bookingEnd = booking.getCheckOut().toLocalDate().isBefore(endDate) ? booking.getCheckOut().toLocalDate() : endDate;

        //Trường hợp ngày checkout bằng ngày bookingEnd thì ko tính 1 đêm
        if (bookingEnd.equals(booking.getCheckOut().toLocalDate())){
            bookingEnd = bookingEnd.minusDays(1);
        }

        if (bookingStart.isBefore(bookingEnd) || bookingStart.isEqual(bookingEnd)) {
            return bookingStart.until(bookingEnd).getDays() +1 ;
        }
        return 0;
    }
    private int calculateGrowth(int current, int previous){
        int growthRate = 0;
        if (previous == 0){
            if (current == 0){
                growthRate = 0;
            } else {
                growthRate = 9999;
            }
        } else {
            growthRate = (current - previous) * 100 / previous;
        }
        return growthRate;
    }
}
