package com.ultimatesoftware.banking.customer.query.service.controllers;

import com.ultimatesoftware.banking.customer.query.domain.models.Customer;
import com.ultimatesoftware.banking.customer.query.service.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("customers")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
        Customer customer = customerRepository.findOne(id);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("customers")
    public String createCustomer(@Valid @RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer.getId();
    }

    @PutMapping("customers/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable("id") String id, @Valid @RequestBody Customer customer) {
        Customer customerFromDB = customerRepository.findOne(id);
        if (customerFromDB != null) {
            customerRepository.save(customer);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("customers/{id}")
    public ResponseEntity deleteCustomers(@PathVariable("id") String id) {
        Customer customerFromDB = customerRepository.findOne(id);
        if (customerFromDB != null) {
            customerRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
