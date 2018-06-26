package com.ultimatesoftware.banking.account.cmd.domain.commands;

public abstract class TransactionCommand implements ICommand {
    protected String transactionId;

    public TransactionCommand(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
