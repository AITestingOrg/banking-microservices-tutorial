package com.ultimatesoftware.banking.account.events;

public abstract class AccountTransactionEvent extends AccountEvent {
    protected String transactionId;

    public AccountTransactionEvent(String id, String transactionId) {
        super(id);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
