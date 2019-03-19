package com.ultimatesoftware.banking.transactions.tests.isolation;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;

import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.transactions.mocks.MockedHttpDependencies;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(environments = {ConfigurationConstants.INTERNAL_MOCKS, ConfigurationConstants.HTTP_MOCKS})
public class WithdrawIsolationTests extends MockedHttpDependencies {
    @Inject
    @Client("/api/v1/transactions")
    RxHttpClient client;

    private static final ObjectId customerId = ObjectId.get();
    private static final ObjectId accountId = ObjectId.get();

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenAmountIsWithdrawed() {
        // Arrange
        customersMock.onCustomerGet_ReturnCustomer(customerId, "John", "Doe");
        accountQueryMock.onAccountGet_ReturnAccount_WithBalance(accountId, customerId, 50.00);
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 15.00);

        // Act
        String response = client.toBlocking().retrieve(HttpRequest.POST("/withdraw", transactionDto));

        // Assert
        assertEquals("Success", response);
    }
}
