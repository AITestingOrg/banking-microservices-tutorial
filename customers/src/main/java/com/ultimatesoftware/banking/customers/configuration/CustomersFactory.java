package com.ultimatesoftware.banking.customers.configuration;

import com.ultimatesoftware.banking.api.factories.MongoRepositoryFactory;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.context.annotation.Factory;

@Factory
public class CustomersFactory extends MongoRepositoryFactory<Customer> {
    public CustomersFactory() {
        super(Customer.class);
    }
}
