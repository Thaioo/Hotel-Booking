package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeReposiory extends JpaRepository<UserEntity,Integer> {
}
