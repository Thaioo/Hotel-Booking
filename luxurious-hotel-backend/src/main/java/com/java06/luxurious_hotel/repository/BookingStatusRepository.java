package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.BookingStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingStatusRepository extends JpaRepository<BookingStatusEntity, Integer> {
}
