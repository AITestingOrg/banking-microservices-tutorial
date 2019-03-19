package com.ultimatesoftware.banking.transactions.tests;

import com.ultimatesoftware.banking.transactions.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.exceptions.NoAccountExistsException;
import org.bson.types.ObjectId;

public class TestConstants {
    public static final double BASE_AMOUNT = 5.0;
    public static final ObjectId ACCOUNT_ID = new ObjectId("5c8ffe2b7c0bec3538855a06");
    public static final ObjectId CUSTOMER_ID = new ObjectId("5c8ffe2b7c0bec3538855a0a");
    public static final ObjectId NO_CUSTOMER_ID = new ObjectId("5c892aecf72465a56c4f816d");
    public static final ObjectId NO_ACCOUNT_ID = new ObjectId("5c8ffe687c0bec35e6e2258f");
    public static final ObjectId DESTINATION_ID = new ObjectId("5c8ffe687c0bec35e6e22589");
    public static final ObjectId TRANSACTION_ID = new ObjectId("5c8ffe687c0bec35e6e2258a");
    public static final String NO_ACCOUNT_MESSAGE = "That isn't here";
    public static final String NO_MONEY_MESSAGE = "The money isn't here";
    public static final NoAccountExistsException ACCOUNT_EXISTS_EXCEPTION
            = new NoAccountExistsException(NO_ACCOUNT_MESSAGE);
    public static final InsufficientBalanceException INSUFFICIENT_BALANCE_EXCEPTION
            = new InsufficientBalanceException(NO_MONEY_MESSAGE);
}
