package com.java06.luxurious_hotel.repository;



import com.java06.luxurious_hotel.dto.RoomAvailableInfo;
import com.java06.luxurious_hotel.dto.RoomBookingInfo;
import com.java06.luxurious_hotel.entity.RoomEntity;
import com.java06.luxurious_hotel.entity.RoomTypeEntity;

import com.java06.luxurious_hotel.entity.RoomEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {



    List<RoomEntity> findRoomEntityByRoomType(RoomTypeEntity roomTypeEntity);



    @Query("SELECT new com.java06.luxurious_hotel.dto.RoomAvailableInfo(r) " +
            "FROM room r join r.roomType rt " +
            "WHERE r.id NOT IN (SELECT rb.room.id FROM booking b " +
            "JOIN b.roomBookings rb WHERE b.checkOut > :checkin AND b.checkIn < :checkout AND b.bookingStatus.id IN (2,3)) ")
    List<RoomAvailableInfo> findAvailableRoom(@Param("checkin") LocalDateTime checkin, @Param("checkout") LocalDateTime checkout);

    RoomEntity findRoomEntityById(int roomId);

    @Query("SELECT rb.room FROM room_booking rb " +
            "JOIN rb.booking b " +
            "WHERE b.checkOut > :checkInDate " +
            "AND b.checkIn < :checkOutDate " +
            "AND b.bookingStatus.id IN (2,3)") //2. Confirmed, 3. Checked in
    List<RoomEntity> findBookedRoomsByDateRange(@Param("checkInDate") LocalDateTime checkInDate,
                                          @Param("checkOutDate") LocalDateTime checkOutDate);

}
