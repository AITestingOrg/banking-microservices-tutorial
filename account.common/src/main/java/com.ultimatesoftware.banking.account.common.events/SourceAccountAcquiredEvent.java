package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class SourceAccountAcquiredEvent {
    private UUID transactionId;
    private UUID id;

    public SourceAccountAcquiredEvent(UUID transactionId, UUID id) {
        this.transactionId = transactionId;
        this.id = id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getId() {
        return id;
    }
}
