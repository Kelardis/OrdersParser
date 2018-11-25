package com.kel.spring.OrdersParser.reader;

import com.kel.spring.OrdersParser.mapper.JsonTransactionLineMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonTransactionItemReader extends TransactionItemReader {

    protected JsonTransactionItemReader() {
        super("json");
        setLineMapper(new JsonTransactionLineMapper());
        setSaveState(false);
    }
}
