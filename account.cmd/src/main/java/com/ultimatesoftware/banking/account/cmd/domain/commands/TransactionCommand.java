package com.ultimatesoftware.banking.account.cmd.domain.commands;

public abstract class TransactionCommand implements Command {
    protected String transactionId;

    public TransactionCommand(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
