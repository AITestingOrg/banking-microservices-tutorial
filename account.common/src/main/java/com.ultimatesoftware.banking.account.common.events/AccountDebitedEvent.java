package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountDebitedEvent {
    private UUID id;
    private double balance;

    public AccountDebitedEvent(UUID id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }
}
