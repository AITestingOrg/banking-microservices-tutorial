package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class AccountReleasedEvent extends AccountTransactionEvent {
    public AccountReleasedEvent(UUID id, String transactionId) {
        super(id, transactionId);
    }
}
