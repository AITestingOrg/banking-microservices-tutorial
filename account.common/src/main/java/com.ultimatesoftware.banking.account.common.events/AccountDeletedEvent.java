package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountDeletedEvent {
    private UUID id;
    private boolean active;

    public AccountDeletedEvent(UUID id, boolean active) {
        this.id = id;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }
}
