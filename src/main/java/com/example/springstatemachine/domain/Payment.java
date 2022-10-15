package com.example.springstatemachine.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

    @Id
    @Generated
    private int id;

    @Enumerated(EnumType.STRING)
    private CheckoutState checkoutState;

    private BigDecimal amount;
}
