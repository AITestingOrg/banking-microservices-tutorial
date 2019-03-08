package com.ultimatesoftware.banking.api.repository;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoRepository<T extends Entity> implements Repository<T> {
    private final String databaseName;
    private final MongoClient mongoClient;
    private final Class<T> type;

    public MongoRepository(MongoClient mongoClient, String databaseName, Class<T> type) {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
        this.type = type;
    }

    @Override public Single<List<T>> findMany() {
        return Flowable.fromPublisher(getCollection().find()).toList();
    }

    @Override public Maybe<T> findOne(String id) {
        return Flowable.fromPublisher(getCollection().find(eq("id", id)).limit(1)).firstElement();
    }

    @Override public Single<T> add(T entity) {
        return findOne(entity.getId())
            .switchIfEmpty(
                Single.fromPublisher(getCollection().insertOne(entity))
                    .map(success -> entity)
            );
    }

    @Override public Maybe<T> replaceOne(String id, T entity) {
        return Flowable.fromPublisher(getCollection().findOneAndReplace(eq("id", id), entity)).firstElement();
    }

    @Override public long deleteOne(String id) {
        return Flowable.fromPublisher(getCollection().deleteOne(eq("id", id))).firstElement().blockingGet().getDeletedCount();
    }

    protected MongoCollection<T> getCollection() {
        return mongoClient
            .getDatabase(databaseName)
            .getCollection(getEntityName(), type);
    }

    private String getEntityName() {
        Class<?> enclosingClass = type.getClass().getEnclosingClass();
        if (enclosingClass != null) {
            return enclosingClass.getName();
        } else {
            return type.getClass().getName();
        }
    }
}
