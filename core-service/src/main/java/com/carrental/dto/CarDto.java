package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long carId;
    private String brand;
    private String model;
    private int year;
    private String description;
    private double rentalRate;
    private String availabilityStatus;
    private String imageUrl;
    private String status;
    private double proposedOwnerRent;
    private LocalDate approvedDate;
    private LocalDate endDate;
    private double ownerBalance;

    private String branchName;
    private String branchLocation;
    private String branchContactInfo;
    private String branchManagerId;
    private String branchManagerName;
    private String ownerName;
    private MultipartFile image;
    private Long ownerId;
    private Long branchId;
}
