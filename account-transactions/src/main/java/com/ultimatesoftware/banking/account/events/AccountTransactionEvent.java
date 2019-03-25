package com.ultimatesoftware.banking.account.events;

import lombok.Getter;

@Getter
public abstract class AccountTransactionEvent extends AccountEvent {
    protected String transactionId;

    public AccountTransactionEvent(String id, String transactionId) {
        super(id);
        this.transactionId = transactionId;
    }
}
