package com.example.springstatemachine.services;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import com.example.springstatemachine.domain.Payment;
import com.example.springstatemachine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<CheckoutState, CheckoutEvent> {

    private final PaymentRepository repository;

    @Override
    public void preStateChange(State<CheckoutState, CheckoutEvent> state, Message<CheckoutEvent> message, Transition<CheckoutState, CheckoutEvent> transition, StateMachine<CheckoutState, CheckoutEvent> stateMachine, StateMachine<CheckoutState, CheckoutEvent> rootStateMachine) {
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Integer.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
                    .ifPresent(paymentId -> {
                        Payment payment = repository.getReferenceById(paymentId);
                        payment.setCheckoutState(state.getId());
                        repository.save(payment);
                    });
        });
    }
}
