package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class DebitAccountCommand extends TransactionCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;
    private double amount;

    public DebitAccountCommand(UUID id, double amount, String transactionId) {
        super(transactionId);
        this.id = id;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}
