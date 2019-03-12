package com.ultimatesoftware.banking.account.query.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;

@Factory
public class AccountsFactory {
    @Value("${micronaut.application.name}")
    private String databaseName;

    @Bean
    public MongoRepository<Account> mongoRepository(MongoClient mongoClient) {
        return new MongoRepository<>(mongoClient, databaseName, Account.class);
    }
}
