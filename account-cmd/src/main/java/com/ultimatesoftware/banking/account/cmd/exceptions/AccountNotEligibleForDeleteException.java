package com.ultimatesoftware.banking.account.cmd.exceptions;

import java.util.UUID;

public class AccountNotEligibleForDeleteException extends Exception {
    private String id;
    private double balance;

    public AccountNotEligibleForDeleteException(String id, double balance) {
    }

    public String getMessage() {
        return String.format("Account %s not eligible for deletion: balance = %f", id, balance);
    }
}
