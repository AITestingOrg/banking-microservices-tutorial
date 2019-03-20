package com.ultimatesoftware.banking.account.query.repositories;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.repository.MockRepository;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import org.bson.types.ObjectId;

import javax.inject.Singleton;

import java.util.ArrayList;

@Primary
@Singleton
@Requires(env = ConfigurationConstants.INTERNAL_MOCKS)
public class MockAccountRepository extends MockRepository<Account> {

    public MockAccountRepository() {
        entities = new ArrayList<>();
        entities.add(new Account(new ObjectId("5c86d04877970c1fd879a36b"), "5c89346ef72465c5981bc1ff", 0.0));
        entities.add(new Account(new ObjectId("5c892dbef72465ad7e7dde42"), "5c86d04877970c1fd879a36b", 10.0));
        entities.add(new Account(new ObjectId("5c89342ef72465c5981bc1fc"), "5c892aecf72465a56c4f816d", 600.0));
    }
}
