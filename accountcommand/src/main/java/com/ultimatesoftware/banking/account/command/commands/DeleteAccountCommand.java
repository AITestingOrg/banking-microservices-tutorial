package com.ultimatesoftware.banking.account.command.commands;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class DeleteAccountCommand implements ICommand {
    @TargetAggregateIdentifier
    private UUID id;
    private boolean active = false;

    public DeleteAccountCommand(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }
}
