package com.ultimatesoftware.banking.account.common.events;

public abstract class AccountTransactionEvent {
    protected String transactionId;

    public AccountTransactionEvent(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
