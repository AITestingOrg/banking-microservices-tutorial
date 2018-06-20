package com.ultimatesoftware.banking.account.cmd.domain;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Transaction;
import com.ultimatesoftware.banking.eventsourcing.handlers.CustomCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;

public class TransactionCommandHandler extends CustomCommandHandler<Transaction> {
    public TransactionCommandHandler(EventSourcingRepository repository, CommandBus commandBus) {
        super(repository, commandBus, Transaction.class);
    }
}
