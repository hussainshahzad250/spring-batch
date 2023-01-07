package com.spring.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringBatchExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchExampleApplication.class, args);
		log.info("Batch application started successfully...");
	}
}
