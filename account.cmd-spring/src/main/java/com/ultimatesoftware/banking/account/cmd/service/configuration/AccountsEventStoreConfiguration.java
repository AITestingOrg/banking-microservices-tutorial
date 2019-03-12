package com.ultimatesoftware.banking.account.cmd.service.configuration;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountsEventStoreConfiguration {
    @Bean
    public Repository<Account> accountEventStore(EventStore eventStore, Cache cache) {
        return EventSourcingRepository.builder(Account.class)
            .eventStore(eventStore)
            .cache(cache)
            .build();
    }

    @Bean
    public Cache cache() {
        return new WeakReferenceCache();
    }
}
