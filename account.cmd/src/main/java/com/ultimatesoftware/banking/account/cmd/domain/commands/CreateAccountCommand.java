package com.ultimatesoftware.banking.account.cmd.domain.commands;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateAccountCommand implements ICommand {
    @TargetAggregateIdentifier
    private UUID id;
    private String customerId;
    private double balance;
    private boolean active;

    public CreateAccountCommand(String customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.balance = 0.0;
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean getActive() {
        return active;
    }
}
