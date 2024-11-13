package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.RoomAmenityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenityEntity, Long> {

    //List<AmenityEntity> findRoomAmenityEntitiesByRoomTypeId(int roomTypeId);
    //List<AmenityEntity> findByRoom_RoomTypeId(int roomtypeId);
    List<RoomAmenityEntity> findByRoomTypeId(int roomId);
    void deleteByRoomTypeId(int roomId);
}
