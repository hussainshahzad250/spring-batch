package com.spring.batch.config;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.spring.batch.entity.Customer;
import com.spring.batch.listener.StepSkipListener;
import com.spring.batch.repository.CustomerRepository;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerItemWriter customerItemWriter;

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> itemReader(@Value("#{jobParameters[filePath]}") String pathToFIle) {
		FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource(new File(pathToFIle)));
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}

	private LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;
	}

	@Bean
	public CustomerProcessor processor() {
		return new CustomerProcessor();
	}

	@Bean
	public RepositoryItemWriter<Customer> writer() {
		RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
		writer.setRepository(customerRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step step1(FlatFileItemReader<Customer> itemReader) {
		return stepBuilderFactory.get("slaveStep").<Customer, Customer>chunk(10).reader(itemReader)
				.processor(processor()).writer(customerItemWriter).faultTolerant().listener(skipListener())
				.skipPolicy(skipPolicy()).taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Job runJob(FlatFileItemReader<Customer> itemReader) {
		return jobBuilderFactory.get("importCustomer").flow(step1(itemReader)).end().build();
	}

	@Bean
	public SkipPolicy skipPolicy() {
		return new ExceptionSkipPolicy();
	}

	@Bean
	public SkipListener<Customer, Number> skipListener() {
		return new StepSkipListener();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
	}

}
