package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.BedTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BedTypeRepository extends JpaRepository<BedTypeEntity,Integer> {

    BedTypeEntity findById(int id);
}
