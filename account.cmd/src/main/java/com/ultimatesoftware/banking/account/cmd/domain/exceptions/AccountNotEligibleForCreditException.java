package com.ultimatesoftware.banking.account.cmd.domain.exceptions;

import java.util.UUID;

public class AccountNotEligibleForCreditException extends Exception {
    private UUID id;
    private double balance;

    public AccountNotEligibleForCreditException(UUID id, Double balance) {
        this.id = id;
        this.balance = balance;
}

    public String getMessage() {
        return String.format("Account %s not eligible for debit: balance = %f.", id, balance);
    }
}
