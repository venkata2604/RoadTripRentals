package com.carrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private double totalAmount;
}
