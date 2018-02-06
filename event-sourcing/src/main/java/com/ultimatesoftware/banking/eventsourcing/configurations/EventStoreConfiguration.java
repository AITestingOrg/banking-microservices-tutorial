package com.ultimatesoftware.banking.eventsourcing.configurations;

import com.mongodb.MongoClient;
import com.ultimatesoftware.banking.eventsourcing.handlers.CustomCommandHandler;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPPublisher;
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
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

public abstract class EventStoreConfiguration<T, K extends CustomCommandHandler<T>> {
    @Value("${spring.data.mongodb.host}")
    protected String host;
    @Value("${spring.data.mongodb.port}")
    protected Integer port;
    @Value("${spring.data.mongodb.username}")
    protected String username;
    @Value("${spring.data.mongodb.database}")
    protected String database;
    @Value("${spring.data.mongodb.password}")
    protected String password;
    @Value("${amqp.events.exchange-name}")
    protected String exchangeName;

    // todo: replace this with something more elegant
    protected Class<T> type;

    public EventStoreConfiguration(Class<T> type) {
        this.type = type;
    }

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
    public EventSourcingRepository<T> eventRepository(EventStore eventStore) {
        return new EventSourcingRepository<>(type, eventStore);
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
    public abstract K commandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus);

    @Bean
    public SpringAMQPPublisher springAMQPPublisher(EventStore eventStore, ConnectionFactory connectionFactory) {
        SpringAMQPPublisher springAMQPPublisher = new SpringAMQPPublisher(eventStore);
        springAMQPPublisher.setConnectionFactory(connectionFactory);
        springAMQPPublisher.setExchangeName(exchangeName);
        springAMQPPublisher.start();
        return springAMQPPublisher;
    }
}
