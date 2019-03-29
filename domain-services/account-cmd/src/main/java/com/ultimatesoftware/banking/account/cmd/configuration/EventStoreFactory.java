package com.ultimatesoftware.banking.account.cmd.configuration;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;
import com.ultimatesoftware.banking.api.factories.AxonEventStoreFactory;
import io.micronaut.context.annotation.Factory;

@Factory
public class EventStoreFactory extends AxonEventStoreFactory<Account> {
    public EventStoreFactory() {
        super(Account.class);
    }
}
