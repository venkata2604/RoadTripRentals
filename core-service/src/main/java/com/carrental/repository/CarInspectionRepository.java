package com.carrental.repository;

import com.carrental.entity.CarInspectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInspectionRepository extends JpaRepository<CarInspectionEntity, Long> {
    // Add custom query methods if needed
}
