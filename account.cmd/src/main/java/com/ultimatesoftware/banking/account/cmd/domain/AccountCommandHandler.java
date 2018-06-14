package com.ultimatesoftware.banking.account.cmd.domain;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.eventsourcing.handlers.CustomCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;

public class AccountCommandHandler extends CustomCommandHandler<Account> {
    public AccountCommandHandler(EventSourcingRepository repository, CommandBus commandBus) {
        super(repository, commandBus, Account.class);
    }
}
