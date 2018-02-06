package com.ultimatesoftware.banking.customerquery.service.controllers;

import com.ultimatesoftware.banking.customerquery.domain.models.Customer;
import com.ultimatesoftware.banking.customerquery.service.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("customers")
    public @ResponseBody
    List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("customers/{id}")
    public @ResponseBody
    Customer getCustomer(@PathVariable("id") String id){
        return customerRepository.findById(id);
    }
}
