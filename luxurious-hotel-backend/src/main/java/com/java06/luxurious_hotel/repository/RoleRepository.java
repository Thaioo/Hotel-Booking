package com.java06.luxurious_hotel.repository;

import com.java06.luxurious_hotel.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findRoleEntitiesById(int id);
    RoleEntity findByName(String name);
}
