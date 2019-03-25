package com.ultimatesoftware.banking.account.transactions.clients;

import com.ultimatesoftware.banking.account.transactions.models.PersonDetailsDto;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

@Client(value = "people-gateway", path = "/api/v1/people")
public interface PeopleDetailsClient {
    @Get("/{id}")
    Maybe<PersonDetailsDto> get(String id);
}
