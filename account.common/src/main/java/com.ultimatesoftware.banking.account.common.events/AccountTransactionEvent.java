package com.ultimatesoftware.banking.account.common.events;

public abstract class AccountTransactionEvent {
    protected boolean success;
    protected String transactionId;

    public AccountTransactionEvent(boolean success, String transactionId) {
        this.success = success;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
