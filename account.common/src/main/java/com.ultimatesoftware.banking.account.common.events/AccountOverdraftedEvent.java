package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountOverdraftedEvent {
    private UUID id;
    private double balance;
    private double debitAmount;

    public AccountOverdraftedEvent(UUID id, double balance, double debitAmount) {
    }

    public UUID getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public double getDebitAmount() {
        return debitAmount;
    }
}
