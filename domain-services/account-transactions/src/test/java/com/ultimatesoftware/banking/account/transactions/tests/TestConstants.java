package com.ultimatesoftware.banking.account.transactions.tests;

import com.ultimatesoftware.banking.account.transactions.exceptions.AccountUpdateException;
import com.ultimatesoftware.banking.account.transactions.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.account.transactions.exceptions.NoAccountExistsException;

public class TestConstants {
    public static final String NO_ACCOUNT_MESSAGE = "That isn't here";
    public static final String NO_MONEY_MESSAGE = "The money isn't here";
    public static final String ACCOUNT_UPDATE_MESSAGE = "An unknown error occurred during account update.";
    public static final NoAccountExistsException ACCOUNT_EXISTS_EXCEPTION
        = new NoAccountExistsException(NO_ACCOUNT_MESSAGE);
    public static final InsufficientBalanceException INSUFFICIENT_BALANCE_EXCEPTION
        = new InsufficientBalanceException(NO_MONEY_MESSAGE);
    public static final AccountUpdateException ACCOUNT_UPDATE_EXCEPTION = new AccountUpdateException(ACCOUNT_UPDATE_MESSAGE);
}
