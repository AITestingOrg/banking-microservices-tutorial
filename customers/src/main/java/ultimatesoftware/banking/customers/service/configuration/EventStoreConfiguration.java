package ultimatesoftware.banking.customers.service.configuration;

import com.mongodb.MongoClient;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ultimatesoftware.banking.customers.domain.commands.CustomerCommandHandler;
import ultimatesoftware.banking.customers.domain.eventhandlers.EventHandler;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

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
    public EventStore eventStore(MongoClient mongoClient) {
        EventStorageEngine eventStorageEngine = new MongoEventStorageEngine(new DefaultMongoTemplate(mongoClient, database));
        return new EmbeddedEventStore(eventStorageEngine);
    }

    @Bean
    public EventSourcingRepository<CustomerAggregate> eventRepository(EventStore eventStore) {
        return new EventSourcingRepository<>(CustomerAggregate.class, eventStore);
    }

    @Bean
    public CommandBus commandBus(AxonConfiguration axonConfiguration) {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.registerHandlerInterceptor(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders()));
        return commandBus;
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }

    @Bean
    public CustomerCommandHandler bankAccountCommandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus) {
        return new CustomerCommandHandler(eventSourcingRepository, commandBus);
    }

    @Bean
    public EventHandler eventHandler(EventStore eventStore) {
        return new EventHandler(eventStore);
    }
}
