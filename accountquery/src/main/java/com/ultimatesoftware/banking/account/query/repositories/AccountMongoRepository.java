package com.ultimatesoftware.banking.account.query.repositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import static com.mongodb.client.model.Filters.eq;

public class AccountMongoRepository extends MongoRepository<Account> {
    public AccountMongoRepository(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName, Account.class);
    }

    public Maybe<Account> findByAccountId(String accountId) {
        return Flowable.fromPublisher(getCollection().find(eq("accountId", accountId)).limit(1)).firstElement();
    }

    public long deleteByAccountId(String accountId) {
        return Flowable.fromPublisher(getCollection().deleteOne(eq("accountId", accountId))).firstElement().blockingGet().getDeletedCount();
    }
}
