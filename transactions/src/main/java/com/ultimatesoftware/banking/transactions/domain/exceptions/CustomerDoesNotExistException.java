package com.ultimatesoftware.banking.transactions.domain.exceptions;

public class CustomerDoesNotExistException extends Exception {
    public CustomerDoesNotExistException(String msg) {
        super(msg);
    }
}
