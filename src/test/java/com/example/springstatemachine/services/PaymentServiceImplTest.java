package com.example.springstatemachine.services;

import com.example.springstatemachine.domain.Payment;
import com.example.springstatemachine.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository repository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("234.90")).build();
    }

    @Transactional
    @Test
    void cardVerification() {
        Payment savePayment = paymentService.newPayment(payment);

        paymentService.cardVerification(savePayment.getId());

        Payment cardVerificationPayment = repository.getOne(savePayment.getId());

        System.out.println(cardVerificationPayment);
    }
}