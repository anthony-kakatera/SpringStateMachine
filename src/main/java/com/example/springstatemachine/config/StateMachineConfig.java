package com.example.springstatemachine.config;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

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
                .and()
                .withExternal().source(CheckoutState.CARD_VERIFICATION).target(CheckoutState.CARD_BALANCE).event(CheckoutEvent.APPROVED_CARD)
                .and()
                .withExternal().source(CheckoutState.CARD_VERIFICATION).target(CheckoutState.CARD_REJECTED).event(CheckoutEvent.DECLINED_CARD);
    }
}
