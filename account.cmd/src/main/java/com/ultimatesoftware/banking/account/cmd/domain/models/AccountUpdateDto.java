package com.ultimatesoftware.banking.account.cmd.domain.models;

import java.util.UUID;

public class AccountUpdateDto {
    private UUID customerId;
    private double balance;
    private boolean active;

    public AccountUpdateDto(UUID customerId, double balance, boolean active) {
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "AccountUpdateDto{" +
                "customerId='" + customerId + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                '}';
    }
}
