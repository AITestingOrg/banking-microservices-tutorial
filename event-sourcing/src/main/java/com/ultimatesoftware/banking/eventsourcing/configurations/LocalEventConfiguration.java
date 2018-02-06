package com.ultimatesoftware.banking.eventsourcing.configurations;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.spring.config.AxonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class LocalEventConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(LocalEventConfiguration.class);

    @Bean
    public EventStorageEngine eventStoreEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    @Qualifier("localSegment")
    public SimpleCommandBus commandBus(AxonConfiguration axonConfiguration) {

        LOG.debug("commandBus(axonConfiguration={})", axonConfiguration);

        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.registerHandlerInterceptor(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders()));
        return commandBus;
    }

    @Bean
    EventBus eventBus(EventStorageEngine eventStorageEngine) {

        LOG.debug("eventBus(eventStorageEngine={}, loggingEventMonitor={})", eventStorageEngine);

        EventBus eventBus = new EmbeddedEventStore(eventStorageEngine);
        return eventBus;
    }
}
