package com.ultimatesoftware.banking.customer.query.service.controllers;

import com.ultimatesoftware.banking.customer.query.domain.models.Customer;
import com.ultimatesoftware.banking.customer.query.service.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("customer")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("customer/{id}")
    public Customer getCustomer(@PathVariable("id") UUID id) {
        return customerRepository.findOne(id);
    }
}
