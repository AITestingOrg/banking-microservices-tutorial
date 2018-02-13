package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class OverDraftAccountCommand {
    @TargetAggregateIdentifier
    private UUID id;
    private double debitAmount;

    public OverDraftAccountCommand(UUID id, double debitAmount) {
        this.id = id;
        this.debitAmount = debitAmount;
    }

    public UUID getId() {
        return id;
    }

    public double getDebitAmount() {
        return debitAmount;
    }
}
