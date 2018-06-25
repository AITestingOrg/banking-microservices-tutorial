package com.ultimatesoftware.banking.transactions;

import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;

import java.util.UUID;

public class TestConstants {
    public static final double baseAmount = 0.0;
    public static final UUID accountId = UUID.randomUUID();
    public static final UUID customerId = UUID.randomUUID();
    public static final UUID destinationId = UUID.randomUUID();
    public static final String transactionID = UUID.randomUUID().toString();
    public static final String noAccountMessage = "That isn't here";
    public static final String noMoneyMessage = "The money isn't here";
    public static final NoAccountExistsException noAccountExistsException
            = new NoAccountExistsException(noAccountMessage);
    public static final InsufficientBalanceException insufficientBalanceException
            = new InsufficientBalanceException(noMoneyMessage);
}
