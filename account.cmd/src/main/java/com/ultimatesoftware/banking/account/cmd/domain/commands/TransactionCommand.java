package com.ultimatesoftware.banking.account.cmd.domain.commands;

import java.util.UUID;

public abstract class TransactionCommand implements Command {
    protected UUID transactionId;

    public TransactionCommand(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
