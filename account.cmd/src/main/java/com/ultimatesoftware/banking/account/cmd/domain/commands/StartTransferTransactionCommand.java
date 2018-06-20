package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class StartTransferTransactionCommand {
    @TargetAggregateIdentifier
    private UUID transactionId;
    private UUID fromAccount;
    private UUID toAccount;
    private double amount;

    public StartTransferTransactionCommand(UUID transactionId, UUID fromAccount, UUID toAccount, double amount) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public StartTransferTransactionCommand(UUID fromAccount, UUID toAccount, double amount) {
        transactionId = UUID.randomUUID();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getFromAccount() {
        return fromAccount;
    }

    public UUID getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }
}
