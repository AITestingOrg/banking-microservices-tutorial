package com.ultimatesoftware.banking.account.cmd.domain.exceptions;

import java.util.UUID;

public class AccountNotEligibleForDeleteException extends Exception {
    private UUID id;
    private double balance;
    private boolean active;

    public AccountNotEligibleForDeleteException(UUID id, double balance, boolean active) {
    }

    public String getMessage() {
        return String.format("Account %s not eligible for deletion: balance = %d, active = %s", id, balance, active);
    }
}
