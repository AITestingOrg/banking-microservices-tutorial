package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class CancelTransferCommand extends TransactionCommand{
    @TargetAggregateIdentifier
    private ObjectId id;
    private String msg;

    public CancelTransferCommand(String id, String transactionId, String msg) {
        super(transactionId);
        this.id =  new ObjectId(id);
        this.msg = msg;
    }
}
