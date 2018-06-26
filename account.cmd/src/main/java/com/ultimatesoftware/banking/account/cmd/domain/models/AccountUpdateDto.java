package com.ultimatesoftware.banking.account.cmd.domain.models;

import java.util.UUID;

public class AccountUpdateDto {
    private UUID customerId;

    public AccountUpdateDto(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AccountUpdateDto{" +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
