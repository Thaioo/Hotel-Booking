package com.java06.luxurious_hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_guest")
    private UserEntity guest;

    @ManyToOne
    @JoinColumn(name = "id_table")
    private TableEntity table;

    @Column(name = "guest_number")
    private int guestNumber;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "reservation")
    private List<OrderEntity> orders;

}
