package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferTransactionStartedEvent extends AccountTransactionEvent {
    private UUID destinationAccountId;
    private double amount;

    public TransferTransactionStartedEvent(UUID id, UUID destinationAccountId, double amount, String transactionId) {
        super(id, transactionId);
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public double getAmount() {
        return amount;
    }
}
