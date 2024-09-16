package com.carrental.repository;

import com.carrental.entity.BranchManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchManagerRepository extends JpaRepository<BranchManagerEntity, Long> {
    Optional<BranchManagerEntity> findByEmail(String email);
}
