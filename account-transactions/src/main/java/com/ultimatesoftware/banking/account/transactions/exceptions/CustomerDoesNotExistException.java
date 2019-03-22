package com.ultimatesoftware.banking.account.transactions.exceptions;

public class CustomerDoesNotExistException extends Exception {
    public CustomerDoesNotExistException(String msg) {
        super(msg);
    }
}
