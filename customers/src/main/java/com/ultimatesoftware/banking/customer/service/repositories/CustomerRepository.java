package com.ultimatesoftware.banking.customer.service.repositories;

import com.ultimatesoftware.banking.customer.domain.models.Customer;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {}
