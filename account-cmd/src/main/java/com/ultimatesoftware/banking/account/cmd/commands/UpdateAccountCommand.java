package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class UpdateAccountCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private String customerId;

    public UpdateAccountCommand(String id, String customerId) {
        this.id =  new ObjectId(id);
        this.customerId = customerId;
    }
}
