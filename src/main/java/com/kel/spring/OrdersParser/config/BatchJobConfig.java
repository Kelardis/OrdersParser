package com.kel.spring.OrdersParser.config;

import com.kel.spring.OrdersParser.entity.Transaction;
import com.kel.spring.OrdersParser.reader.TransactionItemReaderCollector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ApplicationArguments applicationArguments;

    @Autowired
    private TransactionItemReaderCollector transactionItemReaderCollector;

    private Step createStepFromFileName(String filename, ItemWriter<Transaction> writer, TaskExecutor taskExecutor) {
        Resource resource = new FileSystemResource(Paths.get(filename));
        String extension = FilenameUtils.getExtension(filename);
        Step step = null;
        FlatFileItemReader<Transaction> reader = transactionItemReaderCollector.getReaderByFormat(extension);
        if (resource.isReadable() && reader != null) {
            reader.setResource(resource);
            SynchronizedItemStreamReader<Transaction> synchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
            synchronizedItemStreamReader.setDelegate(reader);
            step = stepBuilderFactory
                    .get(extension + "Step")
                    .<Transaction, Transaction>chunk(1)
                    .reader(synchronizedItemStreamReader)
                    .processor((ItemProcessor<Transaction, Transaction>) transaction -> {
                        log.debug("Processing of" + transaction);
                        transaction.setFilename(resource.getFilename());
                        return transaction;
                    })
                    .writer(writer)
                    .taskExecutor(taskExecutor)
                    .build();
        }
        return step;
    }

    @Bean
    public Job parallelBatchJob(ItemWriter<Transaction> writer, TaskExecutor taskExecutor) {
        log.info("Initializing parallelBatchJob");
        Flow[] flows = applicationArguments.getNonOptionArgs().stream()
                .map(filename -> createStepFromFileName(filename, writer, taskExecutor))
                .filter(Objects::nonNull)
                .map(step -> new FlowBuilder<Flow>(step.getName()).from(step).end())
                .toArray(Flow[]::new);

        Flow splitFlow = new FlowBuilder<Flow>("splitFlow")
                .split(taskExecutor)
                .add(flows)
                .build();

        return jobBuilderFactory.get("parallelBatchJob")
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .end()
                .build();
    }

    @Bean
    public ItemWriter<Transaction> transactionItemWriter() {
        return list -> list.forEach(System.out::println); //todo consider if synchronisation required
    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
        asyncTaskExecutor.setConcurrencyLimit(4);
        return asyncTaskExecutor;
    }
}
