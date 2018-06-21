package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountReleasedEvent {
    private UUID transactionId;
    private UUID id;

    public AccountReleasedEvent(UUID transactionId, UUID id) {
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