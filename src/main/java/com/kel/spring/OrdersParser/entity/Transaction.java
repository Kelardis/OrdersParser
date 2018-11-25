package com.kel.spring.OrdersParser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    private int id;
    private int amount;
    private String currency = ""; //is this field in output format or not?
    private String comment = "";
    private String filename = "";
    private int line;
    private String result = "OK";

    @Override
    public String toString() { //todo mb use jackson mapper
        return "{id:" + id + ",amount:" + amount + ",currency:\"" + currency + "\",comment:\"" + comment
                + "\",filename:\"" + filename + "\",line:" + line + ",result:\"" + result + "\"}";
    }
}
