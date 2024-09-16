package com.carrental.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    private String brand;
    private Long branchId;
    private String model;
    private int year;
    private String description;
    private double rentalRate;
    private String availabilityStatus;
    private String imageUrl;
    private Long ownerId;
    private String status;
    private double proposedOwnerRent;
    private LocalDate approvedDate;
    private LocalDate endDate;
    private double ownerBalance;
}
