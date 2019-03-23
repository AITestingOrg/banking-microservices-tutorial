package com.ultimatesoftware.banking.account.transactions.configuration;

import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import com.ultimatesoftware.banking.api.factories.MongoRepositoryFactory;
import io.micronaut.context.annotation.Factory;

@Factory
public class MongoFactory extends MongoRepositoryFactory<Transaction> {
    public MongoFactory() {
        super(Transaction.class);
    }
}
