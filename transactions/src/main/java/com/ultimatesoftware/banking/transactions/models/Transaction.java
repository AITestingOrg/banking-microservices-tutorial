package com.ultimatesoftware.banking.transactions.models;

import com.ultimatesoftware.banking.api.repository.Entity;

import java.util.UUID;

public class Transaction extends Entity {
    private TransactionType type;
    private UUID account;
    private String customerId;
    private double amount;
    private UUID destinationAccount;
    private TransactionStatus status;

    public Transaction() {}

    public Transaction(String id, TransactionType type, UUID account, String customerId, double amount, UUID destinationAccount, TransactionStatus status) {
        super(id);
        this.type = type;
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
        this.status = status;
    }

    public Transaction(String id, TransactionType type, String customerId, UUID account, double amount, UUID destinationAccount) {
        super(id);
        this.type = type;
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
        this.status = TransactionStatus.IN_PROGRESS;
    }

    public UUID getDestinationAccount() {
        return destinationAccount;
    }

    public TransactionType getType() {
        return type;
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

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public static class TransactionBuilder {
        private String id;
        private TransactionType type;
        private UUID account;
        private double amount;
        private UUID destinationAccount;
        private String customerId;

        public TransactionBuilder setType(TransactionType type) {
            this.type = type;
            return this;
        }

        public TransactionBuilder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public TransactionBuilder setAccount(UUID account) {
            this.account = account;
            return this;
        }

        public TransactionBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder setDestinationAccount(UUID destinationAccount) {
            this.destinationAccount = destinationAccount;
            return this;
        }

        public Transaction build() {
            return new Transaction(id, type, customerId, account, amount, destinationAccount);
        }
    }
}
