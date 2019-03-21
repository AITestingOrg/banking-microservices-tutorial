package com.ultimatesoftware.banking.people.cmd.aggregates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
@AllArgsConstructor
public class Person {
    @AggregateIdentifier
    private ObjectId id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
