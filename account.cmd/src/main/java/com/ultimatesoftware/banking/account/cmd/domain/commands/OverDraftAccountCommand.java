package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class OverDraftAccountCommand extends TransactionCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;
    private double debitAmount;

    public OverDraftAccountCommand(UUID id, double debitAmount, UUID transactionId) {
        super(transactionId);
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
