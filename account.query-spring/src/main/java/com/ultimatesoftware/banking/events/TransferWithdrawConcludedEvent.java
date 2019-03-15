package com.ultimatesoftware.banking.events;

import java.util.UUID;

public class TransferWithdrawConcludedEvent extends AccountTransactionEvent {
    private double balance;

    public TransferWithdrawConcludedEvent(UUID id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}
