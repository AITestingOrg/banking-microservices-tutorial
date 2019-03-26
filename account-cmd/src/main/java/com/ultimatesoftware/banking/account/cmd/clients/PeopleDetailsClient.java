package com.ultimatesoftware.banking.account.cmd.clients;

import com.ultimatesoftware.banking.account.cmd.models.PersonDetailsDto;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

@Client(id = "people-gateway")
public interface PeopleDetailsClient {
    @Get("/api/v1/people/{id}")
    Maybe<PersonDetailsDto> get(String id);
}
