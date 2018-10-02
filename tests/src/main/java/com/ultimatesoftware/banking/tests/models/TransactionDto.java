package com.ultimatesoftware.banking.tests.models;

import java.util.UUID;

public class TransactionDto {
    private UUID account;
    private UUID customerId;
    private double amount;
    private UUID destinationAccount;

    public TransactionDto() {}

    public TransactionDto(UUID account, UUID customerId, double amount, UUID destinationAccount) {
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
    }

    public TransactionDto(UUID account, UUID customerId, double amount) {
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
    }

    public UUID getDestinationAccount() {
        return destinationAccount;
    }

    public UUID getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                ", account=" + account +
                ", customerId=" + customerId +
                ", amount=" + amount +
                ", destinationAccount=" + destinationAccount +
                '}';
    }
}
