package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferDepositConcludedEvent extends AccountTransactionEvent{
    private UUID id;
    private double balance;

    public TransferDepositConcludedEvent(UUID id, double balance, String transactionId) {
        super(transactionId);
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
