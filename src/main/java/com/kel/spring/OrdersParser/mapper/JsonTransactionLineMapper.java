package com.kel.spring.OrdersParser.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kel.spring.OrdersParser.entity.JsonTransaction;
import com.kel.spring.OrdersParser.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;

@Slf4j
public class JsonTransactionLineMapper implements LineMapper<Transaction> {

    @Override
    public Transaction mapLine(String line, int lineNumber) throws Exception {
        Transaction result = new Transaction();
        result.setLine(lineNumber);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonTransaction token = objectMapper.readValue(line, JsonTransaction.class);
            result.setId(token.getOrderId());
            result.setCurrency(token.getCurrency());
            result.setAmount(token.getAmount());
            result.setComment(token.getComment());
        } catch (Exception e) {
            log.debug(e.getMessage());
            result.setResult(e.getMessage()); //todo error messages too long. mb should shorten them somehow
        }

        return result;
    }
}
