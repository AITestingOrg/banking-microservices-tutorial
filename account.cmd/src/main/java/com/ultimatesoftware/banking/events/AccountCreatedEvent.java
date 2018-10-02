package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class AccountCreatedEvent extends AccountEvent {
    private String customerId;
    private double balance;

    public AccountCreatedEvent(UUID id, String customerId, double balance) {
        super(id);
        this.customerId = customerId;
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }
}
