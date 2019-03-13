package com.ultimatesoftware.banking.events;

public class TransferCanceledEvent extends AccountTransactionEvent {
    private double balance;
    public TransferCanceledEvent(String id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}
