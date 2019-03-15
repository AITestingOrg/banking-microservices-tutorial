package com.ultimatesoftware.banking.transactions.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
