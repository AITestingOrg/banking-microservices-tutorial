package com.ultimatesoftware.banking.account.cmd.domain.models;

import java.util.UUID;

public class Debit {
    private UUID accountId;
    private double debitAmount;

    public Debit(UUID accountId, double debitAmount) {
        this.accountId = accountId;
        this.debitAmount = debitAmount;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public double getDebitAmount() {
        return debitAmount;
    }
}
