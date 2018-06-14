package com.ultimatesoftware.banking.account.cmd.domain.exceptions;

import java.util.UUID;

public class AccountInactiveException extends Throwable {
    private UUID id;

    public AccountInactiveException(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return String.format("Account %s inactive and not eligible for transaction", id);
    }
}
