package com.ultimatesoftware.banking.customer.query.service.repositories;

import com.ultimatesoftware.banking.customer.query.domain.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CustomerRepository extends MongoRepository<Customer, UUID> {}
