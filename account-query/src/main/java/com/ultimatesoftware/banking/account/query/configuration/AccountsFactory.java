package com.ultimatesoftware.banking.account.query.configuration;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.factories.MongoRepositoryFactory;
import io.micronaut.context.annotation.Factory;

@Factory
public class AccountsFactory extends MongoRepositoryFactory<Account> {
    public AccountsFactory() {
        super(Account.class);
    }
}
