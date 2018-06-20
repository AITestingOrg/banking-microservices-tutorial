package com.ultimatesoftware.banking.transactions.domain.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
