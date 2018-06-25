package com.ultimatesoftware.banking.account.common.events;

import java.util.UUID;

public class DestinationAccountAcquiredEvent extends AccountTransactionEvent {

    public DestinationAccountAcquiredEvent(UUID id, String transactionId) {
        super(id, transactionId);
    }
}
