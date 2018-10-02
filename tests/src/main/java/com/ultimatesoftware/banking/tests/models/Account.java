package com.ultimatesoftware.banking.tests.models;

import java.util.UUID;

public class Account {
    private UUID accountId;
    private String customerId;
    private double balance;

    public Account(UUID accountId, String customerId, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
    }

    public Account() {
    }

    public UUID getId() {
        return accountId;
    }

    public void setId(UUID value) {
        this.accountId = value;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
