package com.carrental.repository;

import com.carrental.entity.CarOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarOwnerRepository extends JpaRepository<CarOwnerEntity, Long> {

    Optional<CarOwnerEntity> findByEmail(String email);
}
