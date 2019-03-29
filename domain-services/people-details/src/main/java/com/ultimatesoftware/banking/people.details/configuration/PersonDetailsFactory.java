package com.ultimatesoftware.banking.people.details.configuration;

import com.ultimatesoftware.banking.api.factories.MongoRepositoryFactory;
import com.ultimatesoftware.banking.people.details.models.PersonDetails;
import io.micronaut.context.annotation.Factory;

@Factory
public class PersonDetailsFactory extends MongoRepositoryFactory<PersonDetails> {
    public PersonDetailsFactory() {
        super(PersonDetails.class);
    }
}
