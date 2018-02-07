package com.ultimatesoftware.banking.customers.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class DeleteCustomerCommand {
    @TargetAggregateIdentifier
    private UUID id;

    public DeleteCustomerCommand(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
