package com.ultimatesoftware.banking.customers.controllers;

import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;

import java.util.List;

@Controller("api/v1")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("customers")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("customers")
    public String createCustomer(@Valid @RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer.getId();
    }

    @PutMapping("customers/{id}")
    public ResponseEntity updateCustomer(@PathVariable("id") String id, @Valid @RequestBody Customer customer) {
        Optional<Customer> customerFromDB = customerRepository.findById(id);
        if (customerFromDB.isPresent()) {
            customerRepository.save(customer);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("customers/{id}")
    public ResponseEntity deleteCustomers(@PathVariable("id") String id) {
        Optional<Customer> customerFromDB = customerRepository.findById(id);
        if (customerFromDB.isPresent()) {
            customerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
