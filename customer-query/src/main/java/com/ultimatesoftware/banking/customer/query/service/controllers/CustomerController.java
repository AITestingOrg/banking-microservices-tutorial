package com.ultimatesoftware.banking.customer.query.service.controllers;

import com.ultimatesoftware.banking.customer.query.domain.models.Customer;
import com.ultimatesoftware.banking.customer.query.service.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api/v1")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("customer")
    public @ResponseBody
    List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("customer/{id}")
    public @ResponseBody Customer getCustomer(@PathVariable("id") UUID id){
        return customerRepository.findOne(id);
    }
}
