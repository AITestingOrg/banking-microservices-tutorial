package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferCanceledEvent extends AccountTransactionEvent {
    public TransferCanceledEvent(UUID id, String transactionId) {
        super(id, transactionId);
    }
}
