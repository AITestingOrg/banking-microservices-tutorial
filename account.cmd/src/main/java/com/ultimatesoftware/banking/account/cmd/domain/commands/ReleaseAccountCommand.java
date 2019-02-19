package com.ultimatesoftware.banking.account.cmd.domain.commands;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ReleaseAccountCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private UUID id;

    public ReleaseAccountCommand(UUID id, String transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
