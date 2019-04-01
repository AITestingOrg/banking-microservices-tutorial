package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

import javax.validation.constraints.Min;

@Getter
public class DebitAccountCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    @Min(value = 0)
    private double amount;
    private boolean transfer;

    public DebitAccountCommand(String id, double amount, String transactionId) {
        super(transactionId);
        this.id =  new ObjectId(id);
        this.amount = amount;
        this.transfer = false;
    }

    public DebitAccountCommand(String id, double amount, String transactionId, boolean transfer) {
        super(transactionId);
        this.id = new ObjectId(id);
        this.amount = amount;
        this.transfer = transfer;
    }
}
