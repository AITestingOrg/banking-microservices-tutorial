package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class SourceAccountAcquiredEvent extends AccountTransactionEvent {
    private UUID id;

    public SourceAccountAcquiredEvent(UUID id, UUID transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
