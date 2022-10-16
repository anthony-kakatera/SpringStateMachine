package com.example.springstatemachine.services;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import com.example.springstatemachine.domain.Payment;
import com.example.springstatemachine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository repository;

    private final StateMachineFactory<CheckoutState, CheckoutEvent> stateMachineFactory;

    @Override
    public Payment newPayment(Payment payment) {
        payment.setCheckoutState(CheckoutState.CARD_VERIFICATION);
        return repository.save(payment);
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardVerification(int paymentId) {
        StateMachine<CheckoutState, CheckoutEvent> stateMachine = build(paymentId);

        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardDeclined(int paymentId) {
        StateMachine<CheckoutState, CheckoutEvent> stateMachine = build(paymentId);

        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardApproved(int paymentId) {
        StateMachine<CheckoutState, CheckoutEvent> stateMachine = build(paymentId);

        return null;
    }

    private StateMachine<CheckoutState, CheckoutEvent> build(int paymentId){
        Payment payment = repository.getOne(paymentId);

        StateMachine<CheckoutState, CheckoutEvent> stateCheckoutEventStateMachine = stateMachineFactory.getStateMachine(Integer.toString(payment.getId()));

        stateCheckoutEventStateMachine.stop();

        stateCheckoutEventStateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getCheckoutState(), null, null, null));
                });

        stateCheckoutEventStateMachine.start();

        return stateCheckoutEventStateMachine;
    }
}
