package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferTransactionStartedEvent {
    private UUID transactionId;
    private UUID id;
    private UUID destinationAccountId;
    private double amount;

    public TransferTransactionStartedEvent(UUID transactionId, UUID id, UUID destinationAccountId, double amount) {
        this.transactionId = transactionId;
        this.id = id;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public double getAmount() {
        return amount;
    }
}
