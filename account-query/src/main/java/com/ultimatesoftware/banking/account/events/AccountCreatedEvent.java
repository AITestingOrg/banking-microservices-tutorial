package com.ultimatesoftware.banking.account.events;

public class AccountCreatedEvent extends AccountEvent {
    private String customerId;
    private double balance;

    public AccountCreatedEvent(String id, String customerId, double balance) {
        super(id);
        this.customerId = customerId;
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }
}
