package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class AccountUpdatedEvent extends AccountEvent {
    private String customerId;

    public AccountUpdatedEvent(UUID id, String customerId) {
        super(id);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
