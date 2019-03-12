package com.ultimatesoftware.banking.transactions.clients;

import com.ultimatesoftware.banking.transactions.models.Transaction;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.reactivex.Flowable;

@Client(id ="account-cmd", path = "/api/v1/accounts/")
@Retryable(attempts = "2")
public interface BankAccountCmdClient {
    @Put("/credit")
    Flowable<String> credit(Transaction transaction);
    @Put("/debit")
    Flowable<String> debit(Transaction transaction);
    @Put("/transfer")
    Flowable<String> transfer(Transaction transaction);
}
