package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class FailTransactionCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private String msg;

    public FailTransactionCommand(String id, String destinationId, String transactionId) {
        super(destinationId);
        this.id = new ObjectId(id);
        this.transactionId = transactionId;
    }

    public FailTransactionCommand(String id, String destinationId, String transactionId, String msg) {
        super(destinationId);
        this.id = new ObjectId(id);
        this.transactionId = transactionId;
        this.msg = msg;
    }
}
