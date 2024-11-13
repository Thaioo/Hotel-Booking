package com.java06.luxurious_hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "room_type")
public class RoomTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "overview")
    private String overview;

    @Column(name = "price")
    private double price;

    @Column(name = "area")
    private double area;

    @Column(name = "capacity")
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "id_bed_type")
    private BedTypeEntity bedType;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "roomType")
    private List<RoomEntity> rooms;

    @OneToMany(mappedBy = "roomType")
    private List<RoomAmenityEntity> roomAmenities;


}
