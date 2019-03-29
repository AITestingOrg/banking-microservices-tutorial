package com.ultimatesoftware.banking.api.operations;

import io.micronaut.http.annotation.Get;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

public interface GetController<T> {
    @Get
    Single<List<T>> getAll();

    @Get("/{id}")
    Maybe<T> get(String id);
}
