package com.kel.spring.OrdersParser.reader;

import com.kel.spring.OrdersParser.mapper.CsvTransactionFieldSetMapper;
import com.kel.spring.OrdersParser.mapper.CsvTransactionLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Component;

@Component
public class CsvTransactionItemReader extends TransactionItemReader {

    public CsvTransactionItemReader() {
        super("csv");
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        CsvTransactionLineMapper lineMapper = new CsvTransactionLineMapper();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CsvTransactionFieldSetMapper());
        setLineMapper(lineMapper);
        setSaveState(false);
    }

}
