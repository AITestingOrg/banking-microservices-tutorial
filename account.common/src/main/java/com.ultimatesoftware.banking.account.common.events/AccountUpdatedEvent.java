package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountUpdatedEvent extends AccountEvent {
    private UUID customerId;

    public AccountUpdatedEvent(UUID id, UUID customerId) {
        super(id);
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
