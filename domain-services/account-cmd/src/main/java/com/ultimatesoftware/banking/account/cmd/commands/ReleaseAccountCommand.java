package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class ReleaseAccountCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;

    public ReleaseAccountCommand(String id, String transactionId) {
        super(transactionId);
        this.id =  new ObjectId(id);
    }
}
