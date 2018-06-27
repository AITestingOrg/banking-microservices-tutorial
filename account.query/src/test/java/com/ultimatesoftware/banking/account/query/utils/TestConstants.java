package com.ultimatesoftware.banking.account.query.utils;

import com.ultimatesoftware.banking.account.query.domain.models.Account;

import java.util.UUID;

public class TestConstants {
    public static final UUID ID = UUID.fromString("7a7d1e99-4823-4aa5-9d3b-2307e88cee08");
    public static final UUID CUSTOMER_ID = UUID.fromString("f6e0ef7e-93af-47e0-b665-e9fbdc184b43");
    public static final Account ACCOUNT = new Account(ID, CUSTOMER_ID, 0);

}
