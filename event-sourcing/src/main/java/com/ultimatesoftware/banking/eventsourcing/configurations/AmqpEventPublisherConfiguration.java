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

public abstract class AmqpEventPublisherConfiguration<T, K extends CustomCommandHandler<T>> extends AmqpConfiguration {
    // todo: replace this with something more elegant
    protected Class<T> type;

    public AmqpEventPublisherConfiguration(Class<T> type) {
        this.type = type;
    }

    @Bean
    public EventStorageEngine eventStorageEngine(PropertiesConfiguration properties) {
        MongoClient mongoClient = new MongoClient(properties.getEventStoreHost(), properties.getEventStorePort());
        return new MongoEventStorageEngine(new DefaultMongoTemplate(mongoClient, properties.getEventStoreDatabase()));
    }

    @Bean
    public EventStore eventStore(EventStorageEngine eventStorageEngine) {
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
    public SpringAMQPPublisher springAMQPPublisher(EventStore eventStore, ConnectionFactory connectionFactory, PropertiesConfiguration properties) {
        SpringAMQPPublisher springAMQPPublisher = new SpringAMQPPublisher(eventStore);
        springAMQPPublisher.setConnectionFactory(connectionFactory);
        springAMQPPublisher.setExchangeName(properties.getExchangeName());
        springAMQPPublisher.start();
        return springAMQPPublisher;
    }
}
