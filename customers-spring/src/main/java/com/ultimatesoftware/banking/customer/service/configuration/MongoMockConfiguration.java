package com.ultimatesoftware.banking.customer.service.configuration;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.net.InetSocketAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@Configuration
public class MongoMockConfiguration {

    private final MongoClient client;
    private final MongoServer server;

    public MongoMockConfiguration() {
        this.server = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();
        this.client = new MongoClient(new ServerAddress(serverAddress));
    }

    @Bean
    public MongoClient mongoClient() {
        return client;
    }
}
