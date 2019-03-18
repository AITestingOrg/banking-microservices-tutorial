package com.ultimatesoftware.banking.transactions.tests.mocks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class MockedHttpDependencies {
    protected CustomersMock customersMock = new CustomersMock();
    protected AccountQueryMock accountQueryMock = new AccountQueryMock();

    @BeforeEach
    public void setup () {
        customersMock.start();
        accountQueryMock.start();
    }

    @AfterEach
    public void teardown () {
        customersMock.stop();
        accountQueryMock.stop();
    }
}
