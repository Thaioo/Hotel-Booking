package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.InvalidTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidTokenEntity, Integer> {
    Optional<InvalidTokenEntity> findByToken(String token);
}
