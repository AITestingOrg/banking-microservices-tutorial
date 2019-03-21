package com.ultimatesoftware.banking.transactions.tests.service.isolation;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.transactions.mocks.HttpClient;
import com.ultimatesoftware.banking.transactions.mocks.MockedHttpDependencies;
import com.ultimatesoftware.banking.transactions.mocks.ResponseDto;
import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import io.micronaut.test.annotation.MicronautTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.ACCOUNT_ID;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.CUSTOMER_ID;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.NO_ACCOUNT_ID;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.NO_CUSTOMER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest()
public class WithdrawTests extends MockedHttpDependencies {

    private static final ObjectId customerId = CUSTOMER_ID;
    private static final ObjectId accountId = ACCOUNT_ID;
    private static final ObjectId noCustomerId = NO_CUSTOMER_ID;
    private static final ObjectId noAccountId = NO_ACCOUNT_ID;

    private static HttpClient client;

    @BeforeAll
    public static void beforeAll() {
        client = HttpClient.getBuilder()
            .setHost("localhost")
            .setPort(8086)
            .setPath("/api/v1/transactions")
            .build();
    }

    /* Happy path tests for withdraw */

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenRequestRespondsWithCreated()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 15.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        assertEquals(201, response.getStatusCode());
        assertTrue(ObjectId.isValid(response.getBody().replace("\n", "")));
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenAmountIsWithdrawedFromAccount()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 25.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        accountCmdService.verify(1, putRequestedFor(urlEqualTo("/api/v1/accounts/debit")));
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenCustomerIsValidated()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 35.39);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        customersService.verify(1, getRequestedFor(urlEqualTo("/api/v1/customers/5c8ffe2b7c0bec3538855a0a")));
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenAccountIsValidated()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 50.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        accountQueryService.verify(1, getRequestedFor(urlEqualTo("/api/v1/accounts/5c8ffe2b7c0bec3538855a06")));
    }

    /* Negative withdraw tests */
    @Test
    public void givenCustomerDoesNotExist_whenWithdraw_thenBadRequest()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(noCustomerId.toHexString(), accountId.toHexString(), 15.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void givenAccountDoesNotExist_whenWithdraw_thenBadRequest()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), noAccountId.toHexString(), 50.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void givenAccountWithBalance_whenWithdrawingMoreThanTheBalance_thenBadRequest()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), noAccountId.toHexString(), 51.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        assertEquals(400, response.getStatusCode());
    }

}
