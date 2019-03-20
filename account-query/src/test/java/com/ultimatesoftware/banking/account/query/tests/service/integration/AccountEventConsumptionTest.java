package com.ultimatesoftware.banking.account.query.tests.service.integration;

import com.ultimatesoftware.banking.account.query.models.Account;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountEventConsumptionTest {
    private static final Logger logger = LoggerFactory.getLogger(AccountEventConsumptionTest.class);
    private final List<ObjectId> activeAccountIds = new ArrayList<>();

    @AfterEach
    public void afterEach() {
        for (ObjectId id: activeAccountIds) {
            deleteAccount(id.toHexString());
        }
        activeAccountIds.clear();
    }

    @Test
    public void givenAccountCreated_whenGettingAccount_ThenAccountIsFound() {
        // Arrange
        String customerId = "test123";
        double balance = 0;
        String accountId = createAccount(customerId, balance);

        // Act
        RestAssured.baseURI = "http://localhost:8084";
        Account account =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/" + accountId)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(Account.class);

        // Assert
        assertEquals(accountId, account.getId().toHexString());
        assertEquals(customerId, account.getCustomerId());
        assertEquals(balance, account.getBalance());
    }

    @Test
    public void givenAccountCreatedCalledWithBalanceGreaterThanZero_whenGettingAccount_ThenAccountIsFoundWithZeroBalance() {
        // Arrange
        String customerId = "test123";
        double balance = 50.00;
        String accountId = createAccount(customerId, balance);

        // Act
        RestAssured.baseURI = "http://localhost:8084";
        Account account =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/" + accountId)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(Account.class);

        // Assert
        assertEquals(accountId, account.getId().toHexString());
        assertEquals(customerId, account.getCustomerId());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void givenNoAccounts_whenGettingAccounts_thenEmptyListReturned() {
        // Arrange

        // Act
        RestAssured.baseURI = "http://localhost:8084";
        List list =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(List.class);

        // Assert
        assertEquals(0, list.size());
    }

    @Test
    public void givenOneAccount_whenGettingAccounts_thenListOfOneReturned() {
        // Arrange
        createAccount("test123", 0.0);
        // Act
        RestAssured.baseURI = "http://localhost:8084";
        List list =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(List.class);

        // Assert
        assertEquals(1, list.size());
    }

    @Test
    public void givenThreeAccounts_whenGettingAccounts_thenListOfThreeReturned() {
        // Arrange
        createAccount("test123", 0.0);
        createAccount("test123", 0.0);
        createAccount("test123", 0.0);
        // Act
        RestAssured.baseURI = "http://localhost:8084";
        List list =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(List.class);

        // Assert
        assertEquals(3, list.size());
    }

    @Test
    public void givenAccountDeleted_whenGettingAccount_thenNoAccountReturned() {
        // Arrange
        String accountId = createAccount("test123", 0.0);
        deleteAccount(accountId);

        // Act
        RestAssured.baseURI = "http://localhost:8084";
        given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/" + accountId)
            .then()
            .statusCode(404);

        // Assert
    }

    private String createAccount(String customerId, double balance) {
        RestAssured.baseURI = "http://localhost:8082";
        String accountId =  given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format("{\"customerId\": \"%s\", \"balance\": %.2f}", customerId, balance))
            .post("/api/v1/accounts")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .body()
            .asString();
        logger.info("Created account with ID: " + accountId);
        activeAccountIds.add(new ObjectId(accountId));
        return accountId;
    }

    private void deleteAccount(String accountId) {
        RestAssured.baseURI = "http://localhost:8082";
        given().urlEncodingEnabled(true)
            .delete("/api/v1/accounts/" + accountId)
            .then()
            .statusCode(200);
        logger.info("Deleted account with ID: " + accountId);
    }
}
