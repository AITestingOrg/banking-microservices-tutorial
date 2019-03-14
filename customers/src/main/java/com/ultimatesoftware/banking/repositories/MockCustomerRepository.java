package com.ultimatesoftware.banking.repositories;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Primary
@Singleton
@Requires(env = ConfigurationConstants.INTERNAL_MOCKS)
public class MockCustomerRepository implements Repository<Customer> {
    private List<Customer> customers;

    public MockCustomerRepository() {
        customers = new ArrayList<>();
        customers.add(new Customer(new ObjectId("5c86d04877970c1fd879a36b"), "Jack", "Oneill"));
        customers.add(new Customer(new ObjectId("5c892dbef72465ad7e7dde42"), "Samantha", "Carter"));
        customers.add(new Customer(new ObjectId("5c89342ef72465c5981bc1fc"), "Daniel", "Jackson"));
    }

    @Override public Single<List<Customer>> findMany() {
        return Single.just(customers);
    }

    @Override public Maybe<Customer> findOne(String id) {
        for(Customer customer: customers) {
            if(customer.getHexId().equals(id)) {
                return Maybe.just(customer);
            }
        }
        return Maybe.never();
    }

    @Override public Single<Customer> add(Customer entity) {
        entity.setId(ObjectId.get());
        customers.add(entity);
        return Single.just(entity);
    }

    @Override public Maybe<UpdateResult> replaceOne(String id, Customer entity) {
        for(int i = 0; i < customers.size(); i++) {
            if(customers.get(i).getHexId().equals(id)) {
                customers.set(i, entity);
                return Maybe.just(UpdateResult.acknowledged(1, 1L, null));
            }
        }
        return Maybe.just(UpdateResult.unacknowledged());
    }

    @Override public Maybe<DeleteResult> deleteOne(String id) {
        for(int i = 0; i < customers.size(); i++) {
            if(customers.get(i).getHexId().equals(id)) {
                customers.remove(i);
                return Maybe.just(DeleteResult.acknowledged(1L));
            }
        }
        return Maybe.just(DeleteResult.unacknowledged());
    }

    @Override public void uniqueKeys(List<String> keys) {

    }
}
