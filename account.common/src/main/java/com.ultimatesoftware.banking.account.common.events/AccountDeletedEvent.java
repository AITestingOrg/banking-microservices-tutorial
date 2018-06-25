package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class AccountDeletedEvent extends AccountEvent {
    public AccountDeletedEvent(UUID id) {
        super(id);
    }
}
