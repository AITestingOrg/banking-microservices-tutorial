package com.ultimatesoftware.banking.account.query.tests.service.isolation;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(environments = ConfigurationConstants.INTERNAL_MOCKS)
public class AccountEndpointTest {
    @Inject
    @Client("/api/v1/accounts")
    RxHttpClient client;

    private final ObjectId id1 = new ObjectId("5c86d04877970c1fd879a36b");
    private final ObjectId id2 = new ObjectId("5c892dbef72465ad7e7dde42");
    private final Account account1 = new Account(id1, "FirstName1", 0.0);
    private final Account account2 = new Account(id2, "FirstName2", 0.0);

    // Given Accounts Exist
    @Test
    public void whenQueryingAAccount_thenReturnThatAccount() {
        // Arrange

        // Act
        Account account = client.toBlocking().retrieve(HttpRequest.GET("/" + account1.getHexId()), Account.class);

        // Assert
        assertEquals(account.getBalance(), account1.getBalance());
        assertEquals(account.getCustomerId(), account1.getCustomerId());
        assertEquals(account.getHexId(), account1.getHexId());
    }

    @Test
    public void whenQueryingAllAccounts_thenReturnAllAccounts() {
        // Arrange

        // Act
        List<Account> accountsFound = (List<Account>) client.toBlocking().retrieve(HttpRequest.GET(""), List.class);

        // Assert
        assertEquals(3, accountsFound.size());
    }

    // Given no accounts exist.

    @Test
    public void whenQueryingAAccount_thenQueryShouldReturnA404() {
        // Arrange

        // Act
        HttpClientResponseException e = Assertions.assertThrows(HttpClientResponseException.class, () -> {
                Account account =
                    client.toBlocking().retrieve(HttpRequest.GET("/" + ObjectId.get()), Account.class);
            });

        // Assert
        assertTrue(e.getStatus().getCode() == 404);
    }
}
