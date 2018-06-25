package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountCreditedEvent extends AccountTransactionEvent {
    private double balance;
    private double creditAmount;
    private UUID customerId;

    public AccountCreditedEvent(UUID id, UUID customerId, double creditAmount, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
        this.creditAmount = creditAmount;
        this.customerId = customerId;
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
