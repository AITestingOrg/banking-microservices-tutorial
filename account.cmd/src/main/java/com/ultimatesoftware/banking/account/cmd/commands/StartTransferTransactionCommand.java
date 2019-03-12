package com.ultimatesoftware.banking.account.cmd.commands;

import com.ultimatesoftware.banking.account.cmd.models.TransactionDto;
import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class StartTransferTransactionCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private UUID id;
    private TransactionDto transactionDto;

    public StartTransferTransactionCommand(TransactionDto transactionDto) {
        super(transactionDto.getId());
        this.id = transactionDto.getAccount();
        this.transactionDto = transactionDto;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getTransactionId() {
        return transactionDto.getId();
    }

    public UUID getDestinationId() {
        return transactionDto.getDestinationAccount();
    }

    public TransactionDto getTransactionDto() {
        return transactionDto;
    }

    public double getAmount() {
        return transactionDto.getAmount();
    }
}
