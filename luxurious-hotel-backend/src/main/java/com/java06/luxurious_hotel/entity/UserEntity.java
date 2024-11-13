package com.java06.luxurious_hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "summary")
    private String summary;

    @Column(name = "image")
    private String image;

    @Column(name = "deleted", columnDefinition = "TINYINT(1)")
    private Integer deleted;

    @Column(name = "enabled",nullable = false)
    private Boolean enabled;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_role")
    private RoleEntity role;

    @ToString.Exclude
    @OneToMany(mappedBy = "guest")
    private List<ReservationEntity> reservations;

    @ToString.Exclude
    @OneToMany(mappedBy = "guest")
    private List<BookingEntity> bookings;
}
