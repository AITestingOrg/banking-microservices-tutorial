package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferFailedToStartEvent {
    private UUID transactionId;

    public TransferFailedToStartEvent(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
