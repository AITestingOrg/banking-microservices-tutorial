package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class TransactionFailedEvent extends AccountTransactionEvent {
    private String msg;

    public TransactionFailedEvent(UUID id, String transactionId, String msg) {
        super(id, transactionId);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
