package com.ultimatesoftware.banking.customer.common.events;

import java.util.UUID;

public class CustomerDeletedEvent {
    protected UUID id;
    public CustomerDeletedEvent(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
