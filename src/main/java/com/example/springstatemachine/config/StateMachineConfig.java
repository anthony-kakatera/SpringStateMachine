package com.example.springstatemachine.config;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

import java.util.EnumSet;

@Slf4j
@EnableStateMachine
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<CheckoutState, CheckoutEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<CheckoutState, CheckoutEvent> states) throws Exception {
        states.withStates()
                .initial(CheckoutState.CARD_VERIFICATION)
                .states(EnumSet.allOf(CheckoutState.class))
                .end(CheckoutState.PAYMENT_APPROVED)
                .end(CheckoutState.PAYMENT_DECLINED)
                .end(CheckoutState.INSUFFICIENT_FUNDS);
    }
}
