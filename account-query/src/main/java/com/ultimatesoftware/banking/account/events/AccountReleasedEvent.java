package com.ultimatesoftware.banking.account.events;

public class AccountReleasedEvent extends AccountTransactionEvent {
    public AccountReleasedEvent(String id, String transactionId) {
        super(id, transactionId);
    }
}
