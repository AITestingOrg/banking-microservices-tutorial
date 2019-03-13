package com.ultimatesoftware.banking.events;

public class TransferDepositConcludedEvent extends AccountTransactionEvent {
    private double balance;

    public TransferDepositConcludedEvent(String id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}
