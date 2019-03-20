package com.ultimatesoftware.banking.transactions.clients;

import com.ultimatesoftware.banking.transactions.models.CustomerDto;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.reactivex.Maybe;

@Client(id = "customers", path = "/api/v1/customers")
public interface CustomerClient {
    @Get("/{id}")
    Maybe<CustomerDto> get(String id);
}
