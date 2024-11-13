package com.java06.luxurious_hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {
    List<RoomTypeDTO> listRoomType;
    List<PaymentStatusDTO> listPaymentStatus;
    List<PaymentMethodDTO> listPaymentMethod;
    List<BookingStatusDTO> listBookingStatus;
}
