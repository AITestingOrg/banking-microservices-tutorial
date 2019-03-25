package com.ultimatesoftware.banking.account.transactions.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
