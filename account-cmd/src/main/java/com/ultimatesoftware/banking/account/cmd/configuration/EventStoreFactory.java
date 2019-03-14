package com.ultimatesoftware.banking.account.cmd.configuration;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.factories.AxonEventStoreFactory;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;

@Factory
@Requires(notEnv = ConfigurationConstants.INTERNAL_MOCKS)
public class EventStoreFactory extends AxonEventStoreFactory<Account> {
    public EventStoreFactory() {
        super(Account.class);
    }
}
