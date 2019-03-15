package com.ultimatesoftware.banking.events;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public abstract class AccountTransactionEvent extends AccountEvent {
    protected String transactionId;

    public AccountTransactionEvent(String id, String transactionId) {
        super(id);
        this.transactionId = transactionId;
    }
}
