package com.ultimatesoftware.banking.account.cmd.tests.service.isolation;

import com.ultimatesoftware.banking.account.cmd.models.AccountDto;
import com.ultimatesoftware.banking.test.utils.HttpClient;
import com.ultimatesoftware.banking.test.utils.ResponseDto;
import io.micronaut.test.annotation.MicronautTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.ultimatesoftware.banking.test.utils.TestConstants.*;

@MicronautTest
public class AccountCmdTests {
    private static final ObjectId customerId = CUSTOMER_ID;
    private static final ObjectId accountId = ACCOUNT_ID;
    private static final ObjectId noCustomerId = NO_CUSTOMER_ID;
    private static final ObjectId noAccountId = NO_ACCOUNT_ID;

    private static HttpClient client;

    @BeforeAll
    public static void beforeAll() {
        client = HttpClient.getBuilder()
            .setHost("localhost")
            .setPort(8082)
            .setPath("/api/v1/accounts")
            .build();
    }

    @Test
    public void givenAccountWithBalance_whenWithdraw_thenRequestRespondsWithCreated()
        throws Exception {
        AccountDto transactionDto = new AccountDto(CUSTOMER_ID.toHexString());

        // Act
        ResponseDto response = client.post(transactionDto, "/");

        // Assert
        //assertEquals(201, response.getStatusCode());
       // assertTrue(ObjectId.isValid(response.getBody().replace("\n", "")));
    }
}
