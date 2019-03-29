package com.ultimatesoftware.banking.people.cmd.configuration;

import com.ultimatesoftware.banking.api.factories.AxonEventStoreFactory;
import com.ultimatesoftware.banking.people.cmd.aggregates.Person;
import io.micronaut.context.annotation.Factory;

@Factory
public class EventStoreFactory extends AxonEventStoreFactory<Person> {
    public EventStoreFactory() {
        super(Person.class);
    }
}
