package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountCreditedEvent {
    private UUID id;
    private UUID customerId;
    private double creditAmount;
    private double balance;

    public AccountCreditedEvent(UUID id, UUID customerId, double creditAmount, double balance) {
        this.id = id;
        this.customerId = customerId;
        this.creditAmount = creditAmount;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public double getCreditAmount() {
        return creditAmount;
    }
}
