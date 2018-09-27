package com.ultimatesoftware.banking.events;

import com.ultimatesoftware.banking.events.AccountTransactionEvent;

import java.util.UUID;

public class TransferDepositConcludedEvent extends AccountTransactionEvent {
    private double balance;

    public TransferDepositConcludedEvent(UUID id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}
