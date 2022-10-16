package com.example.springstatemachine.services;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import com.example.springstatemachine.domain.Payment;
import com.example.springstatemachine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{

    private static final String PAYMENT_ID_HEADER = "payment_id";

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

        sendEvent(paymentId, stateMachine, CheckoutEvent.VERIFYING_CARD);

        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardDeclined(int paymentId) {
        StateMachine<CheckoutState, CheckoutEvent> stateMachine = build(paymentId);

        sendEvent(paymentId, stateMachine, CheckoutEvent.DECLINED_CARD);

        return null;
    }

    @Override
    public StateMachine<CheckoutState, CheckoutEvent> cardApproved(int paymentId) {
        StateMachine<CheckoutState, CheckoutEvent> stateMachine = build(paymentId);

        sendEvent(paymentId, stateMachine, CheckoutEvent.APPROVED_CARD);

        return null;
    }

    private void sendEvent(int paymentId, StateMachine<CheckoutState, CheckoutEvent> sm, CheckoutEvent checkoutEvent){
        Message msg = MessageBuilder.withPayload(checkoutEvent)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();

        sm.sendEvent(msg);
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
