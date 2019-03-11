package com.ultimatesoftware.banking.api.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public interface Repository<T extends Entity> {
    Single<List<T>> findMany();
    Maybe<T> findOne(String id);
    Single<T> add(T entity);
    Maybe<UpdateResult> replaceOne(String id, T entity);
    Maybe<DeleteResult> deleteOne(String id);
}
