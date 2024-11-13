package com.java06.luxurious_hotel.repository;


import com.java06.luxurious_hotel.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity,Integer> {

    //RoomTypeEntity findById(int id);
}
