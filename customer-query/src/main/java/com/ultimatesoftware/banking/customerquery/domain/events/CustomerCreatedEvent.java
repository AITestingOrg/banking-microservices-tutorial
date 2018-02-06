package com.ultimatesoftware.banking.customerquery.domain.events;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CustomerCreatedEvent {
    @NotNull
    private UUID id;

    private String firstName = "";
    private String lastName = "";

    public CustomerCreatedEvent(UUID id, String firstName, String lastName) {
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
