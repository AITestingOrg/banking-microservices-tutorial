package com.ultimatesoftware.banking.account.command.exceptions;

import java.util.UUID;

public class AccountNotEligibleForDebitException extends Exception {
    private UUID id;
    private double balance;

    public AccountNotEligibleForDebitException(UUID id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getMessage() {
        return String.format("Account %s not eligible for debit: balance = %f.", id, balance);
    }
}
