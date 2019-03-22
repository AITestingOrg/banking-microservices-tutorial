package com.ultimatesoftware.banking.account.events;

public class TransferWithdrawConcludedEvent extends AccountTransactionEvent {
    private double balance;

    public TransferWithdrawConcludedEvent(String id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}
