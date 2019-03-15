package com.ultimatesoftware.banking.api.factories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.repository.Entity;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.api.repository.Repository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;

@Requires(beans = MongoClient.class)
@Requires(property = "micronaut.application.name")
@Requires(notEnv = ConfigurationConstants.INTERNAL_MOCKS)
public abstract class MongoRepositoryFactory<T extends Entity> {
    @Value("${micronaut.application.name}")
    private String databaseName;
    private final Class<T> type;

    public MongoRepositoryFactory(Class<T> type) {
        this.type = type;
    }

    @Bean
    @Singleton
    public Repository<T> mongoRepository(MongoClient mongoClient) {
        return new MongoRepository<>(mongoClient, databaseName, type);
    }
}
