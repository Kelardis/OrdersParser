package com.kel.spring.OrdersParser.mapper;

import com.kel.spring.OrdersParser.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;


@Slf4j
public class CsvTransactionLineMapper extends DefaultLineMapper<Transaction> {

    @Override
    public Transaction mapLine(String line, int lineNumber) throws Exception {
        log.debug("String: [" + line + "] lineNumber: " + lineNumber);
        Transaction transaction = super.mapLine(line, lineNumber);
        transaction.setLine(lineNumber);
        return transaction;
    }
}
