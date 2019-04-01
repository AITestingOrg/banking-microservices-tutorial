package com.ultimatesoftware.banking.account.cmd.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class CreditAccountCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    @NotBlank
    private ObjectId id;
    @Min(value = 0)
    private double amount;
    private boolean transfer;

    public CreditAccountCommand(String id, double amount, String transactionId) {
        super(transactionId);
        this.id = new ObjectId(id);
        this.amount = amount;
        this.transfer = false;
    }

    public CreditAccountCommand(String id, double amount, String transactionId, boolean transfer) {
        super(transactionId);
        this.id = new ObjectId(id);
        this.amount = amount;
        this.transfer = transfer;
    }
}
