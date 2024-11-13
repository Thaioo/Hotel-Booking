package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.BookingEntity;
import com.java06.luxurious_hotel.entity.RoomBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    List<BookingEntity> findByCheckOutAfterAndCheckInBefore(LocalDateTime startDate, LocalDateTime endDate);
    List<BookingEntity> findByCheckOutGreaterThanEqualAndCheckInLessThanEqual(LocalDateTime startDate, LocalDateTime endDate);

    List<BookingEntity> findByCheckOutBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> findByGuest_Id(int userId);
    void deleteAllByGuest_Id(int userId);
    boolean existsByGuest_Id(int userId);


    List<BookingEntity> findByGuest_Phone(String phone);
//    List<BookingEntity> findByCheckOutAfterAndCheckInBefore(LocalDateTime inDate, LocalDateTime outDate);
//    List<BookingEntity> findByUser_Id(int userId);

}
