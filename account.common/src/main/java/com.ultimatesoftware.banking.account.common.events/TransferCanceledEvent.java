package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferCanceledEvent extends AccountTransactionEvent {
    public TransferCanceledEvent(String transactionId) {
        super(transactionId);
    }
}
