package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class SourceAccountAcquiredEvent extends AccountTransactionEvent {
    public SourceAccountAcquiredEvent(UUID id, String transactionId) {
        super(id, transactionId);
    }
}
