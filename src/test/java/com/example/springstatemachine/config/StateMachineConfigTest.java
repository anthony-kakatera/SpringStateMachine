package com.example.springstatemachine.config;

import com.example.springstatemachine.domain.CheckoutEvent;
import com.example.springstatemachine.domain.CheckoutState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<CheckoutState, CheckoutEvent> factory;

    //used to test if the state machine responds to events and transitions from one desired state to the next
    @Test
    void testNewStateMachine(){
        StateMachine<CheckoutState, CheckoutEvent> sm = factory.getStateMachine(UUID.randomUUID());

        sm.start();

        //System.out.println(sm.getState().toString());

        //sm.sendEvent(CheckoutEvent.APPROVED_CARD);

        System.out.println(sm.getState().toString());

        sm.sendEvent(CheckoutEvent.DECLINED_CARD);

        System.out.println(sm.getState().toString());
    }
}