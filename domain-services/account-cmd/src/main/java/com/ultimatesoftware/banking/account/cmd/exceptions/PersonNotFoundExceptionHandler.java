package com.ultimatesoftware.banking.account.cmd.exceptions;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

public class PersonNotFoundExceptionHandler implements ExceptionHandler<PersonNotFoundException, HttpResponse> {
    @Override public HttpResponse handle(HttpRequest request, PersonNotFoundException exception) {
        return HttpResponse.notFound("Could not find person with ID " + exception.getPersonId());
    }
}
