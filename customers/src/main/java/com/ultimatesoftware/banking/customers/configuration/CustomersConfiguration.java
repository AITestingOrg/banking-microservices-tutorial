package com.ultimatesoftware.banking.customers.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;

@Factory
@Requires(beans = MongoClient.class)
public class CustomersConfiguration {
    @Value("${micronaut.application.name}")
    private String databaseName;

    @Bean
    public MongoRepository<Customer> mongoRepository(MongoClient mongoClient) {
        return new MongoRepository<>(mongoClient, databaseName, Customer.class);
    }
}
