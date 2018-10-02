package com.ultimatesoftware.banking.tests.models;

import java.util.UUID;

public class AccountCreationDto {
    private UUID customerId;

    public AccountCreationDto(UUID customerId) {
        this.customerId = customerId;
    }

    public AccountCreationDto() {

    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AccountCreationDto{" +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
