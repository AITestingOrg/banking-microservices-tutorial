package com.ultimatesoftware.banking.api.factories;

import io.micronaut.context.annotation.Bean;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.axonserver.connector.AxonServerConnectionManager;
import org.axonframework.axonserver.connector.event.axon.AxonServerEventStore;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateAnnotationCommandHandler;
import org.axonframework.modelling.command.Repository;

import javax.inject.Singleton;

public abstract class AxonEventStoreFactory<T> {
    private final Class<T> type;

    public AxonEventStoreFactory(Class<T> type) {
        this.type = type;
    }

    @Bean
    @Singleton
    public CommandBus commandBus(AxonServerConfiguration axonServerConfiguration) {
        Cache cache = new WeakReferenceCache();
        EventStore eventStore = AxonServerEventStore.builder()
            .configuration(axonServerConfiguration)
            .platformConnectionManager(new AxonServerConnectionManager(axonServerConfiguration))
            .build();
        Repository<T> repository = EventSourcingRepository.builder(type)
            .eventStore(eventStore)
            .cache(cache)
            .build();
        CommandBus commandBus = SimpleCommandBus.builder().build();
        AggregateAnnotationCommandHandler.Builder<T> builder = AggregateAnnotationCommandHandler.builder();
        builder.repository(repository);
        builder.aggregateType(type);
        AggregateAnnotationCommandHandler<T> handler = builder.build();
        handler.subscribe(commandBus);
        return commandBus;
    }

    @Bean
    @Singleton
    public CommandGateway commandGateway(CommandBus commandBus) {
        CommandGateway commandGateway = DefaultCommandGateway.builder()
            .commandBus(commandBus)
            .build();
        return commandGateway;
    }
}
