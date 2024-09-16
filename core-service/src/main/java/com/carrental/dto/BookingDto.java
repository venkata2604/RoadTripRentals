package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long bookingId;
    private Long userId;
    private Long carId;
    private Long pickupLocation;
    private Long dropoffLocation;
    private LocalDate pickupDate;
    private LocalDate dropoffDate;
    private LocalDate actualReturnDate;
    private String bookingStatus;
    private double initialAmount;
    private double finalAmount;
    private String initialAmountStatus;
    private String finalAmountStatus;
    private String carAvailabilityStatus;
    private double totalAmount;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String userName;
    private String userEmail;
    private String pickupLocationName;
    private String dropoffLocationName;
    private String carName;
    private double carRentPerDay;
}
