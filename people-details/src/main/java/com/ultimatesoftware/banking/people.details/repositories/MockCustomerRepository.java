package com.ultimatesoftware.banking.people.details.repositories;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.repository.MockRepository;
import com.ultimatesoftware.banking.people.details.models.PersonDetails;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import org.bson.types.ObjectId;

import javax.inject.Singleton;

import java.util.ArrayList;

@Primary
@Singleton
@Requires(env = ConfigurationConstants.INTERNAL_MOCKS)
public class MockCustomerRepository extends MockRepository<PersonDetails> {

    public MockCustomerRepository() {
        entities = new ArrayList<>();
        entities.add(new PersonDetails(new ObjectId("5c86d04877970c1fd879a36b"), "Jack", "Oneill"));
        entities.add(new PersonDetails(new ObjectId("5c892dbef72465ad7e7dde42"), "Samantha", "Carter"));
        entities.add(new PersonDetails(new ObjectId("5c89342ef72465c5981bc1fc"), "Daniel", "Jackson"));
    }
}
