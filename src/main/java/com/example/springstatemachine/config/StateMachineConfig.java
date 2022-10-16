package com.example.springstatemachine.config;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import com.example.springstatemachine.services.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Random;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<CheckoutState, CheckoutEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<CheckoutState, CheckoutEvent> states) throws Exception {
        states.withStates()
                .initial(CheckoutState.CARD_VERIFICATION)
                .states(EnumSet.allOf(CheckoutState.class))
                .end(CheckoutState.PAYMENT_APPROVED)
                .end(CheckoutState.PAYMENT_DECLINED)
                .end(CheckoutState.CARD_REJECTED)
                .end(CheckoutState.INSUFFICIENT_FUNDS);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<CheckoutState, CheckoutEvent> transitions) throws Exception {
        transitions.withExternal().source(CheckoutState.CARD_VERIFICATION).target(CheckoutState.CARD_VERIFICATION).event(CheckoutEvent.VERIFYING_CARD)
                .action(cardVerificationAction())
                .and()
                .withExternal().source(CheckoutState.CARD_VERIFICATION).target(CheckoutState.CARD_BALANCE).event(CheckoutEvent.APPROVED_CARD)
                .and()
                .withExternal().source(CheckoutState.CARD_VERIFICATION).target(CheckoutState.CARD_REJECTED).event(CheckoutEvent.DECLINED_CARD);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<CheckoutState, CheckoutEvent> config) throws Exception {
        StateMachineListenerAdapter<CheckoutState, CheckoutEvent> adapter = new StateMachineListenerAdapter<CheckoutState, CheckoutEvent>(){
            @Override
            public void stateChanged(State from, State to) {
                log.info(String.format("*** StateChanged(from: %s, to %s)", from, to));
            }
        };
        config.withConfiguration().listener(adapter);
    }

    //This was done to introduce a  random action that will either approve 80% of the time or Decline 20% of the time
    public Action<CheckoutState, CheckoutEvent> cardVerificationAction(){
        return  stateContext -> {
            System.out.println("CARD VERIFICATION CALLED !! ");

            if(new Random().nextInt(10) < 8){
                System.out.println("Approved");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(CheckoutEvent.APPROVED_CARD)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                        .build());
            }else{
                System.out.println("Declined");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(CheckoutEvent.DECLINED_CARD)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                        .build());
            }
        };
    }
}
