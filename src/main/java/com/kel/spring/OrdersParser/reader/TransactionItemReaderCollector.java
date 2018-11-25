package com.kel.spring.OrdersParser.reader;

import com.kel.spring.OrdersParser.entity.Transaction;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class TransactionItemReaderCollector {
    private HashMap<String, TransactionItemReader> itemReaderHashMap;

    public TransactionItemReaderCollector() {
        itemReaderHashMap = new HashMap<>();
    }

    @Autowired
    public void setItemReaders(List<TransactionItemReader> readers) {
        readers.forEach(reader -> {
            itemReaderHashMap.put(reader.getReaderFormat(), reader);
        });
    }

    public FlatFileItemReader<Transaction> getReaderByFormat(String readerFormat) {
        return itemReaderHashMap.get(readerFormat);
    }

}
