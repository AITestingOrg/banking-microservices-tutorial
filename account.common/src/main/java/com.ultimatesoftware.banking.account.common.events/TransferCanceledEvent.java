package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferCanceledEvent extends AccountTransactionEvent {
    public TransferCanceledEvent(UUID transactionId) {
        super(transactionId);
    }
}
