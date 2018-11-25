package com.kel.spring.OrdersParser.mapper;

import com.kel.spring.OrdersParser.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

@Slf4j
public class CsvTransactionFieldSetMapper implements FieldSetMapper<Transaction> {

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
        Transaction transaction = new Transaction();
        try {
            //does line with empty fields counts as failed entry?
            transaction.setId(fieldSet.readInt(0));
            transaction.setAmount(fieldSet.readInt(1));
            transaction.setCurrency(fieldSet.readString(2));
            transaction.setComment(fieldSet.readString(3));
        } catch (ArrayIndexOutOfBoundsException e) {
            //or may be I should check it before filling fields. but then some good fields will be lost
            log.debug(e.getMessage());
            transaction.setResult("Some fields are missing");
        } catch (Exception e) {
            log.debug(e.getMessage());
            transaction.setResult(e.getMessage());
        }
        return transaction;
    }
}
