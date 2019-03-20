package com.ultimatesoftware.banking.transactions.tests.isolation;

import com.ultimatesoftware.banking.transactions.mocks.HttpClient;
import com.ultimatesoftware.banking.transactions.mocks.HttpMockVerifier;
import com.ultimatesoftware.banking.transactions.mocks.ResponseDto;
import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.transactions.mocks.MockedHttpDependencies;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.ultimatesoftware.banking.transactions.tests.TestConstants.ACCOUNT_ID;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.CUSTOMER_ID;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.NO_ACCOUNT_ID;
import static com.ultimatesoftware.banking.transactions.tests.TestConstants.NO_CUSTOMER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithdrawIsolationTests extends MockedHttpDependencies {

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
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenAmountIsWithdrawedFromAccount()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 25.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        HttpMockVerifier accountsCmdMockVerifier = new HttpMockVerifier("localhost", 8082);
        assertEquals(1, accountsCmdMockVerifier.verifyRequestCount("verify_account_cmd_debit_success.json"));
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenCustomerIsValidated()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 35.39);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        HttpMockVerifier accountsCmdMockVerifier = new HttpMockVerifier("localhost", 8085);
        assertEquals(1, accountsCmdMockVerifier.verifyRequestCount("verify_customer_success.json"));
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenAccountIsValidated()
        throws Exception {
        TransactionDto transactionDto = new TransactionDto(customerId.toHexString(), accountId.toHexString(), 50.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/withdraw");

        // Assert
        HttpMockVerifier accountsCmdMockVerifier = new HttpMockVerifier("localhost", 8084);
        assertEquals(1, accountsCmdMockVerifier.verifyRequestCount("verify_account_query_success.json"));
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
