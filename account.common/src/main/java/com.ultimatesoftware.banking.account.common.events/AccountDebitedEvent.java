package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountDebitedEvent extends AccountTransactionEvent {
    private UUID id;
    private double balance;
    private double debitAmount;
    private UUID customerId;

    public AccountDebitedEvent(UUID id, double balance, double debitAmount, UUID customerId, UUID transactionId) {
        super(transactionId);
        this.id = id;
        this.balance = balance;
        this.debitAmount = debitAmount;
        this.customerId = customerId;
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

    public double getDebitAmount() {
        return debitAmount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
