package com.java06.luxurious_hotel.service;

import com.java06.luxurious_hotel.dto.DashboardDTO;

import java.time.LocalDate;

public interface DashboardService {
    DashboardDTO getDashboard(LocalDate startDate, LocalDate endDate);
}
