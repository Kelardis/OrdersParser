package com.kel.spring.OrdersParser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JsonTransaction {
    private int orderId;
    private int amount;
    private String currency;
    private String comment;
}
