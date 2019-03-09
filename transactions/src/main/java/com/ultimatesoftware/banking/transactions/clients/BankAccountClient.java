package com.ultimatesoftware.banking.transactions.clients;

import com.ultimatesoftware.banking.transactions.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.models.Transaction;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Client(id ="accountquery", path = "/api/v1/accounts/")
@Retryable(attempts = "2", delay = "2s")
public interface BankAccountClient {
    Maybe<BankAccountDto> get(String id);
    @Put("/credit")
    Flowable<String> credit(Transaction transaction);
    @Put("/deposit")
    Flowable<String> deposit(Transaction transaction);
    @Put("/transfer")
    Flowable<String> transfer(Transaction transaction);
}
