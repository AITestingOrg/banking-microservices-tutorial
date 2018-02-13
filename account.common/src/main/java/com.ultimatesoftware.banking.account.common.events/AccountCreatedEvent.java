package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountCreatedEvent {
    private UUID id;
    private String customerId;
    private double balance;
    private boolean active;

    public AccountCreatedEvent(UUID id, String customerId, double balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}
