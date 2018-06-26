package com.ultimatesoftware.banking.transactions;

import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;

import java.util.UUID;

public class TestConstants {
    public static final double BASE_AMOUNT = 0.0;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();
    public static final UUID CUSTOMER_ID = UUID.randomUUID();
    public static final UUID DESTINATION_ID = UUID.randomUUID();
    public static final String TRANSACTION_ID = UUID.randomUUID().toString();
    public static final String NO_ACCOUNT_MESSAGE = "That isn't here";
    public static final String NO_MONEY_MESSAGE = "The money isn't here";
    public static final NoAccountExistsException ACCOUNT_EXISTS_EXCEPTION
            = new NoAccountExistsException(NO_ACCOUNT_MESSAGE);
    public static final InsufficientBalanceException INSUFFICIENT_BALANCE_EXCEPTION
            = new InsufficientBalanceException(NO_MONEY_MESSAGE);
}
