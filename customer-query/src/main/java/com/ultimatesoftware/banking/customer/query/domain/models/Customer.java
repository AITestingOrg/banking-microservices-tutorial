package com.ultimatesoftware.banking.customerquery.domain.models;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class Customer {
    @Id
    private UUID id;
    @NotNull
    private final String firstName;
    @NotNull
    private final String lastName;

    public Customer(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
