package com.ultimatesoftware.banking.api.factories;

import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import io.micronaut.configuration.mongo.reactive.DefaultReactiveMongoConfiguration;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;

import javax.inject.Singleton;

@Factory
@Requires(env = ConfigurationConstants.EXTERNAL_MOCKS)
public class MongoTestMockFactory {
    @Primary
    @Bean
    @Singleton
    public MongoClient mongoClient(DefaultReactiveMongoConfiguration defaultReactiveMongoConfiguration) {
        MongoServer server = new MongoServer(new MemoryBackend());
        ServerAddress serverAddress = new ServerAddress(server.bind());
        defaultReactiveMongoConfiguration.setHost(serverAddress);
        return MongoClients.create(defaultReactiveMongoConfiguration.buildSettings());
    }
}
