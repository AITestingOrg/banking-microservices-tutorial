package com.ultimatesoftware.banking.account.cmd.domain.commands;

import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionDto;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class FailToStartTransferTransactionCommand extends TransactionCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;
    private TransactionDto transactionDto;

    public FailToStartTransferTransactionCommand(TransactionDto transactionDto) {
        super(transactionDto.getId());
        this.id = transactionDto.getAccount();
        this.transactionDto = transactionDto;
    }

    public UUID getId() {
        return id;
    }

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
