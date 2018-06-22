package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class DestinationAccountAcquiredEvent extends AccountTransactionEvent {
    private UUID id;

    public DestinationAccountAcquiredEvent(UUID id, UUID transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
