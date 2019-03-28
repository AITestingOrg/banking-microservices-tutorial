package com.ultimatesoftware.banking.api.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.micronaut.context.annotation.Requires;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.bson.types.ObjectId;

import java.util.List;

import static com.ultimatesoftware.banking.api.configuration.ConfigurationConstants.INTERNAL_MOCKS;

@Requires(env = INTERNAL_MOCKS)
public abstract class MockRepository<T extends Entity> implements Repository<T> {
    protected List<T> entities;

    @Override public Single<List<T>> findMany() {
        return Single.just(entities);
    }

    @Override public Maybe<T> findOne(String id) {
        for (T entity: entities) {
            if (entity.getHexId().equals(id)) {
                return Maybe.just(entity);
            }
        }
        return Maybe.empty();
    }

    @Override public Single<T> add(T entity) {
        entity.setId(ObjectId.get());
        entities.add(entity);
        return Single.just(entity);
    }

    @Override public Maybe<UpdateResult> replaceOne(String id, T entity) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getHexId().equals(id)) {
                entities.set(i, entity);
                return Maybe.just(UpdateResult.acknowledged(1, 1L, null));
            }
        }
        return Maybe.just(UpdateResult.unacknowledged());
    }

    @Override public Maybe<DeleteResult> deleteOne(String id) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getHexId().equals(id)) {
                entities.remove(i);
                return Maybe.just(DeleteResult.acknowledged(1L));
            }
        }
        return Maybe.just(DeleteResult.unacknowledged());
    }

    @Override public void uniqueKeys(List<String> keys) {

    }
}
