package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public abstract class AccountTransactionEvent {
    protected UUID transactionId;

    public AccountTransactionEvent(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
