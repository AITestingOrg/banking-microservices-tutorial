package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class CreditAccountCommand {
    @TargetAggregateIdentifier
    private UUID id;
    private double amount;

    public CreditAccountCommand(UUID id, double balance) {
        this.id = id;
        this.amount = balance;
    }

    public UUID getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}
