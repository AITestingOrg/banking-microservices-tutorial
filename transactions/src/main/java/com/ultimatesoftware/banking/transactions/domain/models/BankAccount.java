package com.ultimatesoftware.banking.transactions.domain.models;

import java.util.UUID;

public class BankAccount {
    private UUID id;
    private double balance;
    private UUID customerId;

    public BankAccount() {

    }

    public BankAccount(UUID id, double balance, UUID customerId) {
        this.id = id;
        this.balance = balance;
        this.customerId = customerId;
    }

    public UUID getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
