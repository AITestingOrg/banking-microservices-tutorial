package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class AccountDeletedEvent extends AccountEvent {
    public AccountDeletedEvent(String id) {
        super(id);
    }
}
