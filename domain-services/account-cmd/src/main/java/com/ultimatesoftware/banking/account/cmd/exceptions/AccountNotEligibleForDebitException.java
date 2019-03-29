package com.ultimatesoftware.banking.account.cmd.exceptions;

import java.util.UUID;

public class AccountNotEligibleForDebitException extends Exception {
    private String id;
    private double balance;

    public AccountNotEligibleForDebitException(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getMessage() {
        return String.format("Account %s not eligible for debit: balance = %f.", id, balance);
    }
}
