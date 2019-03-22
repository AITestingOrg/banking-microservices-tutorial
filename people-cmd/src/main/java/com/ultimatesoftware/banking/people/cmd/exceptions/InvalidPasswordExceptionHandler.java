package com.ultimatesoftware.banking.people.cmd.exceptions;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

public class InvalidPasswordExceptionHandler implements ExceptionHandler<InvalidPasswordException, HttpResponse<String>> {

    @Override
    public HttpResponse<String> handle(HttpRequest request, InvalidPasswordException exception) {
        return HttpResponse.badRequest(exception.getMessage());
    }
}
