package com.carrental.paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private Long bookingId;
    private double paymentAmount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
