package com.java06.luxurious_hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "booking")
@DynamicUpdate
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "room_number")
    private int roomNumber;

    @ManyToOne
    @JoinColumn(name = "id_guest")
    private UserEntity guest;

    @Column(name = "adult_number")
    private int adultNumber;

    @Column(name = "children_number")
    private int childrenNumber;

    @ManyToOne
    @JoinColumn(name = "id_payment_status")
    private PaymentStatusEntity paymentStatus;

    @ManyToOne
    @JoinColumn(name = "id_payment")
    private PaymentMethodEntity paymentMethod;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private BookingStatusEntity bookingStatus;

    @Column(name = "paid_amount")
    private double paidAmount;

    @Column(name = "total")
    private double total;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "booking")
    private List<RoomBookingEntity> roomBookings;

}
