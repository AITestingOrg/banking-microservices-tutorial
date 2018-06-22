package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountReleasedEvent extends AccountTransactionEvent{
    private UUID id;

    public AccountReleasedEvent(UUID id, UUID transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}