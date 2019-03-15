package com.ultimatesoftware.banking.events;

import java.util.UUID;

public abstract class AccountTransactionEvent extends AccountEvent {
    protected String transactionId;

    public AccountTransactionEvent(UUID id, String transactionId) {
        super(id);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
