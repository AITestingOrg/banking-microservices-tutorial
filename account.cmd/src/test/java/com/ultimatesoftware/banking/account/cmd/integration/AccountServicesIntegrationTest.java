package com.ultimatesoftware.banking.account.cmd.integration;

import com.mongodb.MongoClient;
import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionDto;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServicesIntegrationTest {
    private final String accountCmd = "http://localhost:8083/api/v1/accounts";
    private final String accountQuery = "http://localhost:8084/api/v1/accounts";

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        MongoClient mongoClient = new MongoClient();
        mongoClient.getDatabase("accountquery").drop();
        mongoClient.getDatabase("accountcmd").drop();
    }

    // Happy tests

    @Test
    public void testWhenCreateAccount_thenAccountIsCreated() throws InterruptedException {
        // Arrange

        // Act
        UUID accountId = createAndGetAccountId(UUID.randomUUID());

        // Assert
        Assert.assertEquals(HttpStatus.OK, checkForAccountStatusCode(accountId));
    }

    @Test
    public void givenAccountExists_WhenCreditAccount_thenBalanceChanges() throws InterruptedException {
        // Arrange
        double balance = 25;
        UUID customerId = UUID.randomUUID();
        UUID accountId = createAndGetAccountId(customerId);

        // Act
        restTemplate.put(URI.create(accountCmd + "/credit"),
                generateTransactionRequest(accountId, customerId, balance, null));

        // Assert
        Assert.assertEquals(balance, checkForBalance(accountId, balance), 1);
    }

    @Test
    public void givenAccountWithEnoughBalance_WhenDebit_thenBalanceChanges() throws InterruptedException {
        // Arrange
        double balance = 40;
        double withdrawAmount = 25;
        double remainingBalance = balance - withdrawAmount;
        UUID customerId = UUID.randomUUID();
        UUID accountId = createAndGetAccountId(customerId);;

        restTemplate.put(URI.create(accountCmd + "/credit"),
                generateTransactionRequest(accountId, customerId, balance, null));

        // Act
        restTemplate.put(URI.create(accountCmd + "/debit"),
                generateTransactionRequest(accountId, customerId, withdrawAmount, null));

        // Assert
        Assert.assertEquals(remainingBalance, checkForBalance(accountId, remainingBalance), 1);
    }

    @Test
    public void givenAccountWithEnoughBalance_WhenTransfer_thenBalanceIsTransfered() throws InterruptedException {
        // Arrange
        double balance = 40;
        double transferAmount = 25;
        double remainingBalance = balance - transferAmount;
        UUID customerId = UUID.randomUUID();
        UUID sourceAccountId = createAndGetAccountId(customerId);
        UUID destinationAccountId = createAndGetAccountId(customerId);

        restTemplate.put(URI.create(accountCmd + "/credit"),
                         generateTransactionRequest(sourceAccountId, customerId, balance, null));

        // Act
        restTemplate.put(URI.create(accountCmd + "/transfer"),
                generateTransactionRequest(sourceAccountId, customerId, transferAmount, destinationAccountId));

        // Assert
        Assert.assertEquals(remainingBalance, checkForBalance(sourceAccountId, remainingBalance), 1);
        Assert.assertEquals(transferAmount, checkForBalance(destinationAccountId, transferAmount), 1);
    }

    @Test
    public void givenAccountExists_WhenDeleteAccount_thenAccountIsDeleted() throws InterruptedException {
        // Arrange
        UUID accountId = createAndGetAccountId(UUID.randomUUID());
        HttpStatus accountCreatedStatus = checkForAccountStatusCode(accountId);

        // Act
        restTemplate.delete(URI.create(accountCmd + "/" + accountId));

        // Assert
        Assert.assertEquals(HttpStatus.OK, accountCreatedStatus);
        Assert.assertNotEquals(HttpStatus.OK, checkForAccountStatusCode(accountId));
    }

    private HttpStatus checkForAccountStatusCode(UUID accountId) throws InterruptedException {
        int i = 0;
        ResponseEntity<Account> queryResponse ;
        do {
            queryResponse = restTemplate.getForEntity(URI.create(accountQuery + "/" + accountId), Account.class);
            if (queryResponse.getStatusCode() == HttpStatus.OK) {
                return HttpStatus.OK;
            } else {
                i++;
                TimeUnit.SECONDS.sleep(1);
            }
        } while(i < 3);

        return queryResponse.getStatusCode();
    }

    private double checkForBalance(UUID accountId, double balance) throws InterruptedException {
        double currentBalance = 0;
        for (int i = 0; i < 3  ; i++) {
            ResponseEntity<Account> queryResponse = restTemplate.getForEntity(URI.create(accountQuery + "/" + accountId), Account.class);
            if (queryResponse.getStatusCode() == HttpStatus.OK ) {
                currentBalance = queryResponse.getBody().getBalance().doubleValue();
                if(currentBalance == balance){
                    return balance;
                }
            } else {
                TimeUnit.SECONDS.sleep(1);
            }
        }
        return currentBalance;
    }

    private UUID createAndGetAccountId(UUID userId) {
        ResponseEntity<String> createResponse
                = restTemplate.postForEntity(URI.create(accountCmd),
                new HttpEntity<>(new AccountCreationDto(userId)),
                String.class);
        return getIdFromResponse(createResponse.getBody());
    }

    private HttpEntity<TransactionDto> generateTransactionRequest(UUID accountId, UUID customerId, double amount, UUID destination){
        TransactionDto transactionDto = new TransactionDto(accountId, customerId, amount, destination);
        transactionDto.setId(accountId.toString());
        return new HttpEntity<>(transactionDto);
    }

    private UUID getIdFromResponse(String resp) {
        if (resp == null || resp.isEmpty()) {
            return null;
        }
        return UUID.fromString(resp.substring(1, resp.length() - 1));
    }
}
