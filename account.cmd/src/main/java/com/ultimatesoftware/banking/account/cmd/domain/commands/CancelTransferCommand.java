package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class CancelTransferCommand extends TransactionCommand{
    @TargetAggregateIdentifier
    private UUID id;

    public CancelTransferCommand(UUID id, String transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}