package com.ultimatesoftware.banking.account.command.exceptions;

import java.util.UUID;

public class AccountActiveException extends Throwable {
    private UUID id;

    public AccountActiveException(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return String.format("Account %s inactive and not eligible for transaction", id);
    }
}
