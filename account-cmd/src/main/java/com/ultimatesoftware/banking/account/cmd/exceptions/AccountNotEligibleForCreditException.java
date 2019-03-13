package com.ultimatesoftware.banking.account.cmd.exceptions;

import java.util.UUID;

public class AccountNotEligibleForCreditException extends Exception {
    private String id;
    private double balance;

    public AccountNotEligibleForCreditException(String id, Double balance) {
        this.id = id;
        this.balance = balance;
}

    public String getMessage() {
        return String.format("Account %s not eligible for credit: balance = %f.", id, balance);
    }
}
