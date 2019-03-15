package com.ultimatesoftware.banking.transactions.exceptions;

public class NoAccountExistsException extends Exception {
    public NoAccountExistsException(String msg) {
        super(msg);
    }
}
