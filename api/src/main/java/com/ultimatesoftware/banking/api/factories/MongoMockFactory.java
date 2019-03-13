package com.ultimatesoftware.banking.api.factories;

import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import io.micronaut.configuration.mongo.reactive.DefaultMongoClientFactory;
import io.micronaut.configuration.mongo.reactive.DefaultReactiveMongoConfiguration;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import javax.inject.Singleton;

@Factory
@Requires(env = "mock")
public class MongoMockFactory {
    private ServerAddress serverAddress;

    @EventListener
    public void onSeverStart(ServerStartupEvent event) {
        MongoServer server = new MongoServer(new MemoryBackend());
        serverAddress = new ServerAddress(server.bind());
    }

    @Primary
    @Bean
    @Singleton
    public MongoClient mongoClient(DefaultReactiveMongoConfiguration defaultReactiveMongoConfiguration) {
        defaultReactiveMongoConfiguration.setHost(serverAddress);
        return MongoClients.create(defaultReactiveMongoConfiguration.buildSettings());
    }
}
