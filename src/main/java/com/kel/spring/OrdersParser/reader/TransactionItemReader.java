package com.kel.spring.OrdersParser.reader;

import com.kel.spring.OrdersParser.entity.Transaction;
import lombok.Getter;
import org.springframework.batch.item.file.FlatFileItemReader;


public abstract class TransactionItemReader extends FlatFileItemReader<Transaction> {

    @Getter
    private String readerFormat;

    protected TransactionItemReader(String readerFormat) {
        this.readerFormat = readerFormat;
    }
}
