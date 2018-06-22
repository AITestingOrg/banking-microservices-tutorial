package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountOverdraftedEvent extends AccountTransactionEvent {
    private UUID id;
    private double balance;
    private UUID customerId;
    private double debitAmount;

    public AccountOverdraftedEvent(UUID id, double balance, double debitAmount, UUID customerId, UUID transactionId) {
        super(transactionId);
        this.id = id;
        this.balance = balance;
        this.customerId = customerId;
        this.debitAmount = debitAmount;
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

    public UUID getCustomerId() {
        return customerId;
    }
}
