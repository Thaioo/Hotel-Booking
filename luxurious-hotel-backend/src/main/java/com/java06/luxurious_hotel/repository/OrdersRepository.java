package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntity,Integer> {
    void deleteAllByReservation_Guest_Id(int guestId);
    boolean existsByReservation_Guest_Id(int guestId);
}
