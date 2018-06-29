package com.ultimatesoftware.banking.customer.service.repositories;

import com.ultimatesoftware.banking.customer.domain.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {}
