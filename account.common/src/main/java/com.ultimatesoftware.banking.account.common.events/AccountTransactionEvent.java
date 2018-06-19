package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public abstract class AccountTransactionEvent {
    protected boolean success;
    protected UUID transactionId;

    public AccountTransactionEvent(boolean success, UUID transactionId) {
        this.success = success;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
