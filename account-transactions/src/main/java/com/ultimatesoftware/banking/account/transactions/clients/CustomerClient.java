package com.ultimatesoftware.banking.account.transactions.clients;

import com.ultimatesoftware.banking.account.transactions.models.CustomerDto;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

@Client(id = "customers", path = "/api/v1/customers")
public interface CustomerClient {
    @Get("/{id}")
    Maybe<CustomerDto> get(String id);
}
