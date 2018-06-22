package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class StartTransferDepositCommand extends TransactionCommand {
    @TargetAggregateIdentifier
    private UUID id;
    private double amount;

    public StartTransferDepositCommand(UUID transactionId, UUID id, double amount) {
        super(transactionId);
        this.id = id;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}