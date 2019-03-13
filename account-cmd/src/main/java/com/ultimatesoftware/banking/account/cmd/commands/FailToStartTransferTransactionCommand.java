package com.ultimatesoftware.banking.account.cmd.commands;

import com.ultimatesoftware.banking.account.cmd.models.TransactionDto;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class FailToStartTransferTransactionCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private TransactionDto transactionDto;

    public FailToStartTransferTransactionCommand(TransactionDto transactionDto) {
        super(transactionDto.getId());
        this.id =  new ObjectId(transactionDto.getAccountId());
        this.transactionDto = transactionDto;
    }
}
