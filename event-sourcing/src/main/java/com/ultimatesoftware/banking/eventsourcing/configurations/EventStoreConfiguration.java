package com.ultimatesoftware.banking.eventsourcing.configurations;

import com.mongodb.MongoClient;
import com.ultimatesoftware.banking.eventsourcing.handlers.CommandHandler;
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
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

public abstract class EventStoreConfiguration<T, K extends CommandHandler<T>> {
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
    @Value("${amqp.events.exchange-name}")
    protected String exchangeName;

    // todo: replace this with something more elgant
    protected Class<T> type;

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
    public SpringAMQPPublisher springAMQPPublisher(EventStore eventStore, ConnectionFactory connectionFactory, Serializer serializer) {
        SpringAMQPPublisher springAMQPPublisher = new SpringAMQPPublisher(eventStore);
        springAMQPPublisher.setConnectionFactory(connectionFactory);
        springAMQPPublisher.setExchangeName(exchangeName);
        springAMQPPublisher.start();
        return springAMQPPublisher;
    }
}
