package com.ultimatesoftware.banking.account.transactions.configuration;

import com.ultimatesoftware.banking.api.factories.MongoRepositoryFactory;
import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import io.micronaut.context.annotation.Factory;

@Factory
public class MongoFactory extends MongoRepositoryFactory<Transaction> {
    public MongoFactory() {
        super(Transaction.class);
    }
}
