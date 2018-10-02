package com.ultimatesoftware.banking.tests.models;

import java.util.UUID;

public class Customer {
    private UUID id;
    private String firstName;
    private String lastName;

    public Customer(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer() {
    }

    public void setId(UUID value) {
        this.id = value;
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
