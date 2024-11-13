package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Integer> {
    void deleteAllByGuestId(int guestId);
    boolean existsByGuestId(int guestId);
}
