package com.ultimatesoftware.banking.account.cmd.domain.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class CreateAccountCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;
    private UUID customerId;
    private double balance;
    private boolean active;

    public CreateAccountCommand(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.balance = 0.0;
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean getActive() {
        return active;
    }
}
