package com.ultimatesoftware.banking.account.cmd.domain.commands;

import java.util.UUID;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class AcquireSourceAccountCommand extends TransactionCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;

    public AcquireSourceAccountCommand(UUID id, UUID transactionId) {
        super(transactionId);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
