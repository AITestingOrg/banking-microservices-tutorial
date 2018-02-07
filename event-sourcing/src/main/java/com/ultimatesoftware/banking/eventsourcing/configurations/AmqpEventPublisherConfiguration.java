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
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

/**
 * Default configuration for AMQP enabled event publishing and CommandGateway provider.
 * The default configuration uses MongoDB as the event store.
 * @param <T> Aggregate
 * @param <K> Aggregate's Command Handler
 */
public abstract class AmqpEventPublisherConfiguration<T, K extends CustomCommandHandler<T>> extends AmqpConfiguration {
    /** todo: replace this with something more elegant, used to pass the generic type to the event store.
    **/
    protected Class<T> type;

    /**
     *  Constructor for providing the Aggregate's type.
     * @param type Aggregate Type
     */
    public AmqpEventPublisherConfiguration(Class<T> type) {
        this.type = type;
    }

    /**
     * EventStoreEngine provider, by default using MongoDB. This can be overriden for other EventStorageEngine types.
     * @param properties PropertiesConfiguration for MongoDB connection information.
     * @return EventStorageEngine
     */
    @Bean
    public EventStorageEngine eventStorageEngine(PropertiesConfiguration properties) {
        MongoClient mongoClient = new MongoClient(properties.getEventStoreHost(), properties.getEventStorePort());
        return new MongoEventStorageEngine(new DefaultMongoTemplate(mongoClient, properties.getEventStoreDatabase()));
    }

    /**
     * By default, configured with EmbeddedEventStore. Establishes the event store.
     * @param eventStorageEngine provided EventStorageEngine
     * @return EventStore configured with provided EventStorageEngine.
     */
    @Bean
    public EventStore eventStore(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

    /**
     * Configures the EventStore with the Aggregate type.
     * @param eventStore provided EventStore
     * @return EventSourcingRepository
     */
    @Bean
    public EventSourcingRepository<T> eventRepository(EventStore eventStore) {
        return new EventSourcingRepository<>(type, eventStore);
    }

    /**
     * Provides the CommandBus, by default, configured with a SimpleCommandBus
     * including Correlation IDs.
     * @param axonConfiguration Auto Axon Configuration
     * @return provided CommandBus
     */
    @Bean
    public CommandBus commandBus(AxonConfiguration axonConfiguration) {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.registerHandlerInterceptor(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders()));
        return commandBus;
    }

    /**
     * Provides a DefaultCommandGateway using the CommandBus.
     * @param commandBus Provided CommandBus
     * @return the CommandGateway for applying commands.
     */
    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }

    /**
     * Abstract method for subscribing the EventSource to the CommandBus.
     * Left as abstract to be configured by the consumer.
     * @param eventSourcingRepository the provided event sourcing repository.
     * @param commandBus The provided CommandBus
     * @return CommandHandler subscribed to the command bus and event source repository.
     */
    @Bean
    public abstract K commandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus);

    /**
     * Provides the SpringAMQPPublisher which is bound to the EventStore, automatically sending
     * events onto the AMQP exchange.
     * @param eventStore Provided EventStore
     * @param connectionFactory Provided Connection Factory
     * @param properties PropertiesConfiguration
     * @return The SpringAMQPPublisher
     */
    @Bean
    public SpringAMQPPublisher springAMQPPublisher(EventStore eventStore, ConnectionFactory connectionFactory, PropertiesConfiguration properties) {
        SpringAMQPPublisher springAMQPPublisher = new SpringAMQPPublisher(eventStore);
        springAMQPPublisher.setConnectionFactory(connectionFactory);
        springAMQPPublisher.setExchangeName(properties.getExchangeName());
        springAMQPPublisher.start();
        return springAMQPPublisher;
    }
}
