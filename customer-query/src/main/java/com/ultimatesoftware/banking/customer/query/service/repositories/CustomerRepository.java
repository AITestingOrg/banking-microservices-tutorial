package com.ultimatesoftware.banking.customer.query.service.repositories;

import com.ultimatesoftware.banking.customer.query.domain.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findById(String id);
}
