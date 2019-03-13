package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class CreateAccountCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private String customerId;
    private double balance;
    private boolean active;

    public CreateAccountCommand(String customerId) {
        this.id = ObjectId.get();
        this.customerId = customerId;
        this.balance = 0.0;
        this.active = true;
    }
}
