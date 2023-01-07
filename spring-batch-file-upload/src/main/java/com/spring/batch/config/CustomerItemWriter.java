package com.spring.batch.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.batch.entity.Customer;
import com.spring.batch.repository.CustomerRepository;

@Component
public class CustomerItemWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        System.out.println("Writer Thread "+Thread.currentThread().getName());
        repository.saveAll(list);
    }
}
