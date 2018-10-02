package com.ultimatesoftware.banking.tests.models;

import java.util.UUID;

public class TransactionDto {
    private String id;
    private UUID account;
    private String customerId;
    private double amount;
    private UUID destinationAccount;

    public TransactionDto() {}

    public TransactionDto(UUID account, String customerId, double amount, UUID destinationAccount) {
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
    }

    public TransactionDto(String id, UUID account, String customerId, double amount) {
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getCustomerId() {
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
