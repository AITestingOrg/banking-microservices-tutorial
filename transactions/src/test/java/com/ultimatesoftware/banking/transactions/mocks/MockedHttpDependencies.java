package com.ultimatesoftware.banking.transactions.mocks;

import io.micronaut.http.client.RxHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class MockedHttpDependencies {
    private final RxHttpClient httpClient;
    protected final CustomersMock customersMock = new CustomersMock();
    protected final AccountQueryMock accountQueryMock = new AccountQueryMock();

    @BeforeEach
    public void setup () {
        // todo: start mocks then register with consolue
        customersMock.start();
        accountQueryMock.start();
    }

    @AfterEach
    public void teardown () {
        customersMock.stop();
        accountQueryMock.stop();
    }
}
