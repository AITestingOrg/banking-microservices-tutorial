package com.ultimatesoftware.banking.api.repository;

import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public interface Repository<T extends Entity> {
    Single<List<T>> findMany();
    Maybe<T> findOne(String id);
    Single<T> add(T entity);
    Maybe<T> replaceOne(String id, T entity);
    long deleteOne(String id);
}
