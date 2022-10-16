package com.example.springstatemachine.services;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import com.example.springstatemachine.domain.Payment;
import org.springframework.statemachine.StateMachine;

public class PaymentServiceImpl implements PaymentService{
    @Override
    public Payment newPayment(Payment payment) {
        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardVerification(int paymentId) {
        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardDeclined(int paymentId) {
        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardApproved(int paymentId) {
        return null;
    }
}
