package com.example.springstatemachine.services;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import com.example.springstatemachine.domain.Payment;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    Payment newPayment(Payment payment);

    StateMachine<CheckoutState, CheckoutEvent> cardVerification(int paymentId);

    StateMachine<CheckoutState, CheckoutEvent> cardDeclined(int paymentId);

    StateMachine<CheckoutState, CheckoutEvent> cardApproved(int paymentId);
}
