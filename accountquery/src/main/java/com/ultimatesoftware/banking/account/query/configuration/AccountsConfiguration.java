package com.ultimatesoftware.banking.account.query.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.ultimatesoftware.banking.account.query.repositories.AccountMongoRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class AccountsConfiguration {
    private final String databaseName = "banking";

    @Bean
    public AccountMongoRepository mongoRepository(MongoClient mongoClient) {
        return new AccountMongoRepository(mongoClient, databaseName);
    }
}
