package com.carrental.paymentservice.service;

import com.carrental.paymentservice.dto.PaymentDto;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getPayments();

    PaymentDto getPayment(Long id);

    PaymentDto addPayment(PaymentDto paymentDto);

    PaymentDto updatePayment(Long id, PaymentDto updatedPaymentDto);

    void deletePayment(Long id);
}
