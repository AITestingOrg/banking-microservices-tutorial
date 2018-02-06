package com.ultimatesoftware.banking.customerscmd.domain.events;

import java.util.UUID;

public class CustomerDeletedEvent {
    protected UUID id;
    public CustomerDeletedEvent(UUID id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }
}
