package com.ultimatesoftware.banking.account.query.utils;

import com.ultimatesoftware.banking.account.query.models.Account;
import org.bson.types.ObjectId;

public class TestConstants {
    public static final ObjectId ID = new ObjectId("5c8ffe2b7c0bec3538855a06");
    public static final String CUSTOMER_ID = "5c8ffe2b7c0bec3538855a0a";
    public static final Account ACCOUNT = new Account(ID, CUSTOMER_ID, 0);
}
