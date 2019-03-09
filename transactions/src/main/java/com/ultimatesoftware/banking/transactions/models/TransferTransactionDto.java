package com.ultimatesoftware.banking.transactions.models;

import java.util.UUID;

public class TransferTransactionDto extends TransactionDto {
    private UUID destinationAccountId;

    public TransferTransactionDto(String customerId, UUID accountId, Double amount,
        UUID destinationAccountId) {
        super(customerId, accountId, amount);
        this.destinationAccountId = destinationAccountId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }
}
