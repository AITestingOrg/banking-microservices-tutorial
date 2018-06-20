package com.ultimatesoftware.banking.account.cmd.service.configuration;

import com.ultimatesoftware.banking.account.cmd.domain.TransactionCommandHandler;
import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Transaction;
import com.ultimatesoftware.banking.eventsourcing.configurations.AmqpEventPublisherConfiguration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.context.annotation.Bean;

public class TransactionsEventStoreConfiguration extends AmqpEventPublisherConfiguration<Transaction, TransactionCommandHandler> {
    public TransactionsEventStoreConfiguration() {
        super(Transaction.class);
    }

    @Override
    @Bean
    public TransactionCommandHandler commandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus) {
        TransactionCommandHandler commandHandler = new TransactionCommandHandler(eventSourcingRepository, commandBus);
        commandHandler.subscribe();
        return commandHandler;
    }
}
