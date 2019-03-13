package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class DeleteAccountCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private boolean active = false;

    public DeleteAccountCommand(String id) {
        this.id =  new ObjectId(id);
    }
}
