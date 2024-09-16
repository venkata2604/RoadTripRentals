package com.carrental.paymentservice.serviceImpl;

import com.carrental.paymentservice.dto.PaymentDto;
import com.carrental.paymentservice.entity.PaymentEntity;
import com.carrental.paymentservice.exception.PaymentNotFoundException;
import com.carrental.paymentservice.repository.PaymentRepository;
import com.carrental.paymentservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PaymentDto> getPayments() {
        List<PaymentEntity> payments = paymentRepository.findAll();
        return payments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public PaymentDto getPayment(Long id) throws PaymentNotFoundException {
        Optional<PaymentEntity> paymentEntity = paymentRepository.findById(id);
        if (paymentEntity.isEmpty()) {
            throw new PaymentNotFoundException("Payment not found");
        } else {
            return mapToDto(paymentEntity.get());
        }
    }

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        PaymentEntity paymentEntity = modelMapper.map(paymentDto, PaymentEntity.class);
        PaymentEntity savedPaymentEntity = paymentRepository.save(paymentEntity);
        return mapToDto(savedPaymentEntity);
    }

    @Override
    public PaymentDto updatePayment(Long id, PaymentDto updatedPaymentDto) throws PaymentNotFoundException {
        Optional<PaymentEntity> existingPaymentEntity = paymentRepository.findById(id);
        if (existingPaymentEntity.isEmpty()) throw new PaymentNotFoundException("Payment not found");

        PaymentEntity updatedPaymentEntity = modelMapper.map(updatedPaymentDto, PaymentEntity.class);
        updatedPaymentEntity.setPaymentId(id);
        paymentRepository.save(updatedPaymentEntity);

        return mapToDto(updatedPaymentEntity);
    }

    @Override
    public void deletePayment(Long id) throws PaymentNotFoundException {
        Optional<PaymentEntity> existingPaymentEntity = paymentRepository.findById(id);
        if (existingPaymentEntity.isEmpty()) throw new PaymentNotFoundException("Payment not found");
        paymentRepository.deleteById(id);
    }

    private PaymentDto mapToDto(PaymentEntity paymentEntity) {
        return modelMapper.map(paymentEntity, PaymentDto.class);
    }
}
