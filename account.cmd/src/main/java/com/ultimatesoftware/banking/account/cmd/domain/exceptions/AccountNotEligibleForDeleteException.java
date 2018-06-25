package com.ultimatesoftware.banking.account.cmd.domain.exceptions;

import java.util.UUID;

public class AccountNotEligibleForDeleteException extends Exception {
    private UUID id;
    private double balance;

    public AccountNotEligibleForDeleteException(UUID id, double balance) {
    }

    public String getMessage() {
        return String.format("Account %s not eligible for deletion: balance = %f", id, balance);
    }
}
