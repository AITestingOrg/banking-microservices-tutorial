package com.ultimatesoftware.banking.api.operations;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.api.repository.Entity;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;

public interface CrudController<T extends Entity> {
    @Post
    Single<T> create(@Valid @Body T entity);

    @Put("/{id}")
    Maybe<UpdateResult> update(String id, @Valid @Body T entity);

    @Delete("/{id}")
    Maybe<DeleteResult> delete(String id);
}
