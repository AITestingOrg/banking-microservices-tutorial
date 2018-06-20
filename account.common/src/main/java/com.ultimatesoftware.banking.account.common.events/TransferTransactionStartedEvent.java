package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferTransactionStartedEvent {
    private UUID transactionId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private double ammount;

    public TransferTransactionStartedEvent(UUID transactionId, UUID fromAccountId, UUID toAccountId, double ammount) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.ammount = ammount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return ammount;
    }
}
