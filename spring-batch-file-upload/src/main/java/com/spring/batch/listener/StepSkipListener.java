package com.spring.batch.listener;

import org.springframework.batch.core.SkipListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.batch.entity.Customer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepSkipListener implements SkipListener<Customer, Number> {


	@Override
	public void onSkipInRead(Throwable throwable) {
		log.info("A failure on read {} ", throwable.getMessage());
	}

	@Override
	public void onSkipInWrite(Number item, Throwable throwable) {
		log.info("A failure on write {} , {}", throwable.getMessage(), item);
	}

	@SneakyThrows
	@Override
	public void onSkipInProcess(Customer customer, Throwable throwable) {
		log.info("Item {}  was skipped due to the exception  {}", new ObjectMapper().writeValueAsString(customer),
				throwable.getMessage());
	}
}
