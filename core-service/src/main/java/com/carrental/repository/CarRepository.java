package com.carrental.repository;

import com.carrental.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
    List<CarEntity> findAllByBranchId(Long branchId);
    @Query("SELECT COALESCE(MAX(c.carId), 0) FROM CarEntity c")
    Long findMaxCarId();

    List<CarEntity> findAllByOwnerId(Long ownerId);
}
