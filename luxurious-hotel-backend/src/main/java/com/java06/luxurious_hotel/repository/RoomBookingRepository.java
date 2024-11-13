package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.BookingEntity;
import com.java06.luxurious_hotel.entity.RoomAmenityEntity;
import com.java06.luxurious_hotel.entity.RoomBookingEntity;

import com.java06.luxurious_hotel.entity.RoomEntity;
import com.java06.luxurious_hotel.entity.keys.RoomBookingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBookingEntity, RoomBookingKey> {

    boolean existsByRoomId(int id);
    void deleteAllByRoomId(int id);


    List<RoomBookingEntity> findByBooking(BookingEntity booking);
    int deleteByBooking(BookingEntity bookingEntity);
    void deleteAllByBooking_Guest_Id(int id);
    boolean existsByBooking_Guest_Id(int id);

    //List<BookingEntity> findBookingByRoom(RoomEntity room);


    @Query("SELECT rb FROM room_booking rb " +
            "JOIN booking b " +
            "WHERE :selectDate BETWEEN b.checkIn AND b.checkOut " +
            "AND b.bookingStatus.id IN (2,3,4)")
    List<RoomBookingEntity> findRoomBookingsBySelectedDate(@Param("selectDate") LocalDateTime selectDate);



//    List<RoomAmenityEntity> findByRoomTypeId(int roomId);
//    void deleteByRoomTypeId(int roomId);

}
