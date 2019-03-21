package com.ultimatesoftware.banking.transactions.exceptions;

public class CustomerDoesNotExistException extends Exception {
    public CustomerDoesNotExistException(String msg) {
        super(msg);
    }
}
