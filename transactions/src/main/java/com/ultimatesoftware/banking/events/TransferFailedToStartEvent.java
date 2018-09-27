package com.ultimatesoftware.banking.events;

import com.ultimatesoftware.banking.events.AccountTransactionEvent;

import java.util.UUID;

public class TransferFailedToStartEvent extends AccountTransactionEvent{
    public TransferFailedToStartEvent(UUID id, String transactionId) {
        super(id, transactionId);
    }
}
