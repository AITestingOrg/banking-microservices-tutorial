package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountCreditedEvent extends AccountTransactionEvent {
    private UUID id;
    private double balance;
    private double creditAmount;
    private UUID customerId;

    public AccountCreditedEvent(UUID id, double balance, double creditAmount, UUID customerId, UUID transactionId) {
        super(transactionId);
        this.id = id;
        this.balance = balance;
        this.creditAmount = creditAmount;
        this.customerId = customerId;
    }

    public UUID getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
