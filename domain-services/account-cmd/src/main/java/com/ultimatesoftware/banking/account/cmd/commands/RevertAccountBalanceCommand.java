package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class RevertAccountBalanceCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private double amount;
    private String transactionId;

    public RevertAccountBalanceCommand(String id, double amount, String transactionId) {
        this.id = new ObjectId(id);
        this.amount = amount;
        this.transactionId = transactionId;
    }
}
