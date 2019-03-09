package com.ultimatesoftware.banking.transactions.models;

import java.util.UUID;

public class BankAccountDto {
    private UUID id;
    private double balance;
    private String customerId;

    public BankAccountDto() {

    }

    public BankAccountDto(UUID id, double balance, String customerId) {
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

    public String getCustomerId() {
        return customerId;
    }
}
