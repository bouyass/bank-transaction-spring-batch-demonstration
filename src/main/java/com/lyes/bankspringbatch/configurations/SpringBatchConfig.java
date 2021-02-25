package com.lyes.bankspringbatch.configurations;

import com.lyes.bankspringbatch.models.BankTransaction;
import com.lyes.bankspringbatch.repositories.BankTransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<BankTransaction> itemReader;
    @Autowired private ItemProcessor<BankTransaction, BankTransaction> itemProcessor;
    @Autowired private ItemWriter<BankTransaction> itemWriter;


    @Bean
    public Job bankJob(){
        Step step = stepBuilderFactory.get("step1")
                    .<BankTransaction, BankTransaction>chunk(100)
                    .reader(itemReader)
                    .processor(itemProcessor)
                    .writer(itemWriter)
                    .build();

        return jobBuilderFactory.get("job").start(step).build();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> getItemReader(@Value("inputFile") Resource resource){
        FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("CSV-READER");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }


    @Bean
    public LineMapper<BankTransaction> lineMapper(){
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("transactionId","transactionAccountId","strTransactionDate","transactionType","transactionAmount");
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ItemProcessor<BankTransaction, BankTransaction> itemProcessor(){
        return new ItemProcessor<BankTransaction, BankTransaction>() {
            private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy-HH:mm");
            @Override
            public BankTransaction process(BankTransaction bankTransaction) throws Exception {
                bankTransaction.setTransactionDate(simpleDateFormat.parse(bankTransaction.getStrTransactionDate()));
                return bankTransaction;
            }
        };
    }

    @Bean
    public ItemWriter<BankTransaction> getItemWriter(){
        return new ItemWriter<BankTransaction>() {
            @Autowired
            private BankTransactionRepository bankTransactionRepository;
            @Override
            public void write(List<? extends BankTransaction> list) throws Exception {
                bankTransactionRepository.saveAll(list);
            }
        };
    }




}

