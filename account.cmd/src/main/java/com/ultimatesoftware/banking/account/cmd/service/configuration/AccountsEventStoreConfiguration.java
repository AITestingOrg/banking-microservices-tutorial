package com.ultimatesoftware.banking.account.cmd.service.configuration;

import com.ultimatesoftware.banking.account.cmd.domain.AccountCommandHandler;
import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.eventsourcing.configurations.AmqpEventPublisherConfiguration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountsEventStoreConfiguration extends AmqpEventPublisherConfiguration<Account, AccountCommandHandler> {
    public AccountsEventStoreConfiguration() {
        super(Account.class);
    }

    @Override
    @Bean
    public AccountCommandHandler commandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus) {
        AccountCommandHandler commandHandler = new AccountCommandHandler(eventSourcingRepository, commandBus);
        commandHandler.subscribe();
        return commandHandler;
    }
}
