package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class AcquireDestinationAccountCommand extends TransactionCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;

    public AcquireDestinationAccountCommand(UUID id, String transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
