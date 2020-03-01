package com.tradeai.priceloadbatch.config;




import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.tradeai.priceloadbatch.datatransfer.SecurityPriceDTO;
import com.tradeai.priceloadbatch.datatransfer.SecurityPriceInputDTO;
import com.tradeai.priceloadbatch.listener.JobCompletionNotificationListener;
import com.tradeai.priceloadbatch.processor.INFYPriceItemProcessor;
import com.tradeai.priceloadbatch.processor.TCSPriceItemProcessor;


@Configuration
@EnableBatchProcessing

public class LoadPriceConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1, Step step2) {
		return jobBuilderFactory.get("import Asset Job").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1).next(step2).end().build();
	}
	
	

	@Bean
	public Step step1(JdbcBatchItemWriter<SecurityPriceDTO> writer) {
		return stepBuilderFactory.get("step1").<SecurityPriceInputDTO, SecurityPriceDTO>chunk(10).reader(readerTCS()).processor(processor())
				.writer(writer).build();
	}
	
	@Bean
	public Step step2(JdbcBatchItemWriter<SecurityPriceDTO> writer) {
		return stepBuilderFactory.get("step2").<SecurityPriceInputDTO, SecurityPriceDTO>chunk(10).reader(readerInfy()).processor(processorINFY())
				.writer(writer).build();
	}
	
	

	@Bean
	public FlatFileItemReader<SecurityPriceInputDTO> readerTCS() {
		
		return new FlatFileItemReaderBuilder<SecurityPriceInputDTO>().name("Stock Price Item Reader")
				
				.resource(new ClassPathResource("TCS.csv")).delimited()
				
				.names(new String[] { "Date", "Close"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<SecurityPriceInputDTO>() {
					{
						setTargetType(SecurityPriceInputDTO.class);
					}
				})
				.linesToSkip(1)
				.build();
	}
	
	
	@Bean
	public FlatFileItemReader<SecurityPriceInputDTO> readerInfy() {
		
		return new FlatFileItemReaderBuilder<SecurityPriceInputDTO>().name("Stock Price Item Reader")
				
				.resource(new ClassPathResource("INFY.csv")).delimited()
				
				.names(new String[] { "Date", "Close"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<SecurityPriceInputDTO>() {
					{
						setTargetType(SecurityPriceInputDTO.class);
					}
				})
				.linesToSkip(1)
				.build();
	}


	@Bean
	public TCSPriceItemProcessor processor() {
		return new TCSPriceItemProcessor();
	}
	
	@Bean
	public INFYPriceItemProcessor processorINFY() {
		return new INFYPriceItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<SecurityPriceDTO> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<SecurityPriceDTO>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO price.price  VALUES (:stockId, :price, :dateOfPrice)").dataSource(dataSource).build();
	}

}
