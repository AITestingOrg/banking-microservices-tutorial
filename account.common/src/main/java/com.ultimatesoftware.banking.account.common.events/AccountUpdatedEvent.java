package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountUpdatedEvent {
    private UUID id;
    private UUID customerId;

    public AccountUpdatedEvent(UUID id, UUID customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
