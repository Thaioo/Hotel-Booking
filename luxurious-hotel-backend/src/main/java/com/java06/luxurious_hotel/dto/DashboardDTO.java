package com.java06.luxurious_hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private int totalVisitor;
    private int growthVisitor;
    private int totalBooking;
    private int growthBooking;
    private double totalRevenue;
    private int growthRevenue;
    private int occupancyRate;
    private int growthOccupancy;

    private int preVisitor;
    private int preBooking;
    private double preRevenue;
    private int previousOccupancyRate;

}
