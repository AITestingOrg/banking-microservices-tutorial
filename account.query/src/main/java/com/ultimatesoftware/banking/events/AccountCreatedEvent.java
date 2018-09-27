package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class AccountCreatedEvent extends AccountEvent {
    private UUID customerId;
    private double balance;

    public AccountCreatedEvent(UUID id, UUID customerId, double balance) {
        super(id);
        this.customerId = customerId;
        this.balance = balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }
}
