package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class DestinationAccountAcquiredEvent {
    private UUID transactionId;
    private UUID id;

    public DestinationAccountAcquiredEvent(UUID transactionId, UUID id) {
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
