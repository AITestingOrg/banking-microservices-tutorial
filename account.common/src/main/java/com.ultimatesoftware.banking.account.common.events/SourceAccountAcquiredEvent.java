package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class SourceAccountAcquiredEvent extends AccountTransactionEvent {
    private UUID id;

    public SourceAccountAcquiredEvent(UUID id, String transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
