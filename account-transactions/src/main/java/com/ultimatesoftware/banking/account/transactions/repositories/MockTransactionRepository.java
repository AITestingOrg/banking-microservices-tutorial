package com.ultimatesoftware.banking.account.transactions.repositories;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.repository.MockRepository;
import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import com.ultimatesoftware.banking.account.transactions.models.TransactionStatus;
import com.ultimatesoftware.banking.account.transactions.models.TransactionType;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import org.bson.types.ObjectId;

import javax.inject.Singleton;

import java.util.ArrayList;

@Primary
@Singleton
@Requires(env = ConfigurationConstants.EXTERNAL_MOCKS)
public class MockTransactionRepository extends MockRepository<Transaction> {

    public MockTransactionRepository() {
        entities = new ArrayList<>();
        entities.add(new Transaction(new ObjectId("5c86d04877970c1fd879a36b"), TransactionType.CREDIT, "5c86d04877970c1fd879a36b", "5c89346ef72465c5981bc1ff", 0.0, null, TransactionStatus.FAILED));
        entities.add(new Transaction(new ObjectId("5c892dbef72465ad7e7dde42"), TransactionType.DEBIT, "5c892dbef72465ad7e7dde42", "5c86d04877970c1fd879a36b", 10.0, null, TransactionStatus.IN_PROGRESS));
        entities.add(new Transaction(new ObjectId("5c89342ef72465c5981bc1fc"), TransactionType.TRANSFER, "5c89342ef72465c5981bc1fc", "5c892aecf72465a56c4f816d", 600.0, null, TransactionStatus.SUCCESSFUL));
    }
}
