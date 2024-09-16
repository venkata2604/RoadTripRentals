package com.carrental.paymentservice.controller;

import com.carrental.paymentservice.dto.PaymentDto;
import com.carrental.paymentservice.exception.PaymentNotFoundException;
import com.carrental.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getPayments() {
        List<PaymentDto> payments = paymentService.getPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        try {
            PaymentDto payment = paymentService.getPayment(id);
            return ResponseEntity.ok(payment);
        } catch (PaymentNotFoundException ex) {
            // Handle the exception here, such as returning a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PaymentDto> addPayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto newPayment = paymentService.addPayment(paymentDto);
        return ResponseEntity.ok(newPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Long id, @RequestBody PaymentDto updatedPaymentDto) {
        try {
            PaymentDto updatedPayment = paymentService.updatePayment(id, updatedPaymentDto);
            return ResponseEntity.ok(updatedPayment);
        } catch (PaymentNotFoundException ex) {
            // Handle the exception here, such as returning a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        } catch (PaymentNotFoundException ex) {
            // Handle the exception here, such as returning a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
    }
}
