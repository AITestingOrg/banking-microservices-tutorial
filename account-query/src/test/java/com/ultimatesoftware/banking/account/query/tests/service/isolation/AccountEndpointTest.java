package com.ultimatesoftware.banking.account.query.tests.service.isolation;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.repository.Repository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest()
public class AccountEndpointTest {
    @Inject
    @Client("/api/v1/accounts")
    RxHttpClient client;

    @Inject
    Repository<Account> mongoRepository;

    private final ObjectId id1 = new ObjectId("5c86d04877970c1fd879a36b");
    private final ObjectId id2 = new ObjectId("5c892dbef72465ad7e7dde42");
    private final Account account1 = new Account(id1, "5c89346ef72465c5981bc1ff", 0.0);
    private final Account account2 = new Account(id2, "5c89346ef72465c5981bc1fa", 0.0);

    @BeforeEach
    public void beforeEach() {
        List<String> accountIds = mongoRepository.findMany().blockingGet()
            .stream()
            .map(account -> account.getHexId()).collect(Collectors.toList());
        accountIds.forEach(id -> mongoRepository.deleteOne(id).blockingGet());
        mongoRepository.add(account1).blockingGet();
        mongoRepository.add(account2).blockingGet();
    }

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
        assertEquals(2, accountsFound.size());
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
