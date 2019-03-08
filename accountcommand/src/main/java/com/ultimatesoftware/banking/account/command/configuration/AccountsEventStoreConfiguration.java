package com.ultimatesoftware.banking.account.command.configuration;

import com.ultimatesoftware.banking.account.command.aggregates.Account;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
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

@Factory
public class AccountsEventStoreConfiguration {
    private CommandGateway commandGateway;
    private CommandBus commandBus;

    public AccountsEventStoreConfiguration() {
        Cache cache = new WeakReferenceCache();
        AxonServerConfiguration axonServerConfiguration = AxonServerConfiguration.builder()
            .componentName("Account.Command")
            .servers("localhost")
            .build();
        EventStore eventStore = AxonServerEventStore.builder()
            .configuration(axonServerConfiguration)
            .platformConnectionManager(new AxonServerConnectionManager(axonServerConfiguration))
            .build();
        Repository<Account> repository = EventSourcingRepository.builder(Account.class)
            .eventStore(eventStore)
            .cache(cache)
            .build();
        commandBus = SimpleCommandBus.builder().build();
        AggregateAnnotationCommandHandler.Builder<Account> builder = AggregateAnnotationCommandHandler.builder();
        builder.repository(repository);
        builder.aggregateType(Account.class);
        AggregateAnnotationCommandHandler<Account> handler = builder.build();
        handler.subscribe(commandBus);
        commandGateway = DefaultCommandGateway.builder()
            .commandBus(commandBus)
            .build();
    }

    @Bean
    public CommandBus commandBus() {
        return commandBus;
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return commandGateway;
    }
}
