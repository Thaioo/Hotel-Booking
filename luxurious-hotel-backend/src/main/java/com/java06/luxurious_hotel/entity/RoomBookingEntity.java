package com.java06.luxurious_hotel.entity;

import com.java06.luxurious_hotel.entity.keys.RoomBookingKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "room_booking")
public class RoomBookingEntity {

    @EmbeddedId
    private RoomBookingKey roomBookingKey = new RoomBookingKey();

    @ManyToOne
    @MapsId("idRoom")
    @JoinColumn(name = "id_room")
    private RoomEntity room;

    @ManyToOne
    @MapsId("idBooking")
    @JoinColumn(name = "id_booking")
    private BookingEntity booking;
}
