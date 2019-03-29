package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class DebitAccountCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private double amount;

    public DebitAccountCommand(String id, double amount, String transactionId) {
        super(transactionId);
        this.id =  new ObjectId(id);
        this.amount = amount;
    }
}
