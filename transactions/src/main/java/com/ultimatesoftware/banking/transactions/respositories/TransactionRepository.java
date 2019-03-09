package com.ultimatesoftware.banking.transactions.respositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.transactions.models.Transaction;

public class TransactionRepository extends MongoRepository<Transaction> {
    private static final String databaseName = "banking";
    public TransactionRepository(MongoClient mongoClient) {
        super(mongoClient, databaseName, Transaction.class);
    }
}
