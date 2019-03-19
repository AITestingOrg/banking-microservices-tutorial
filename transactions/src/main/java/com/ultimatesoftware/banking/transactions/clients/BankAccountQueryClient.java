package com.ultimatesoftware.banking.transactions.clients;

import com.ultimatesoftware.banking.transactions.models.BankAccountDto;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.reactivex.Maybe;

@Client(id ="account-query", path = "/api/v1/accounts")
public interface BankAccountQueryClient {
    @Get("/{id}")
    Maybe<BankAccountDto> get(String id);
}
