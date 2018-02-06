package com.ultimatesoftware.banking.customerquery.service.repositories;

import com.ultimatesoftware.banking.customerquery.domain.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
