package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class TransferFailedToConcludeEvent {
    private UUID transactionId;

    public TransferFailedToConcludeEvent(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
