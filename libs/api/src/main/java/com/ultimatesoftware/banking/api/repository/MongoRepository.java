package com.ultimatesoftware.banking.api.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        return Flowable.fromPublisher(getCollection().find(eq("_id", new ObjectId(id))).limit(1)).firstElement();
    }

    @Override public Single<T> add(T entity) {
        if (entity.getId() == null) {
            entity.setId(ObjectId.get());
        }
        return findOne(entity.getId().toHexString())
            .switchIfEmpty(
                Single.fromPublisher(getCollection().insertOne(entity))
                    .map(success -> entity)
            );
    }

    @Override public Maybe<UpdateResult> replaceOne(String id, T entity) {
        return Flowable.fromPublisher(getCollection().replaceOne(eq("_id", new ObjectId(id)), entity)).firstElement();
    }

    @Override public Maybe<DeleteResult> deleteOne(String id) {
        return Flowable.fromPublisher(getCollection().deleteOne(eq("_id", new ObjectId(id)))).firstElement();
    }

    @Override public void uniqueKeys(List<String> keys) {
        keys.forEach(key -> getCollection().createIndex(new Document(key, 1), new IndexOptions().unique(true)));
    }

    protected MongoCollection<T> getCollection() {
        return mongoClient
            .getDatabase(databaseName)
            .getCollection(getEntityName(), type);
    }

    private String getEntityName() {
        return type.getSimpleName();
    }
}
