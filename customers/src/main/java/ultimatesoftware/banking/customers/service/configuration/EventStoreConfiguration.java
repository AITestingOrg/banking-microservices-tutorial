package ultimatesoftware.banking.customers.service.configuration;

import com.mongodb.MongoClient;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class EventStoreConfiguration {
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private Integer port;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public MongoClient mongo() throws UnknownHostException {
        return new MongoClient(host, port);
    }

    @Bean
    public EventStorageEngine eventStore(MongoClient mongoClient) {
        return new MongoEventStorageEngine(new DefaultMongoTemplate(mongoClient, database));
    }

    @Bean
    public CommandGateway commandGateway() {
        return new DefaultCommandGateway(new SimpleCommandBus());
    }
}
