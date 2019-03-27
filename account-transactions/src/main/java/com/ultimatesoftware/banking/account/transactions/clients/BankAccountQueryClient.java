package com.ultimatesoftware.banking.account.transactions.clients;

import com.ultimatesoftware.banking.account.transactions.models.BankAccountDto;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

@Client(value = "account-query", path = "/api/v1/accounts")
public interface BankAccountQueryClient {
    @Get("/{id}")
    Maybe<BankAccountDto> get(String id);
}
