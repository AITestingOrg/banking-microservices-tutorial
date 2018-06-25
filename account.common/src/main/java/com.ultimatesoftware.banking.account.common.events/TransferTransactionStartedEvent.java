package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferTransactionStartedEvent extends AccountTransactionEvent {
    private UUID id;
    private UUID destinationAccountId;
    private double amount;

    public TransferTransactionStartedEvent(UUID id, UUID destinationAccountId, double amount, String transactionId) {
        super(transactionId);
        this.id = id;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public String getTransactionId() {
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
