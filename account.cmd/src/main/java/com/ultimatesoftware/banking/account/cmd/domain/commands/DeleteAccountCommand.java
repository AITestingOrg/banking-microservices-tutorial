package com.ultimatesoftware.banking.account.cmd.domain.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class DeleteAccountCommand implements Command {
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
