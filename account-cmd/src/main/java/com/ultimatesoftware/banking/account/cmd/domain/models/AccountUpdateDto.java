package com.ultimatesoftware.banking.account.cmd.domain.models;

public class AccountUpdateDto {
    private String customerId;
    private double balance;
    private boolean active;

    public AccountUpdateDto(String customerId, double balance, boolean active) {
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}
