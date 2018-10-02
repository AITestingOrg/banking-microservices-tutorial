package com.ultimatesoftware.banking.tests.edge;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.ultimatesoftware.banking.tests.models.Account;
import com.ultimatesoftware.banking.tests.models.AccountCreationDto;
import com.ultimatesoftware.banking.tests.models.Customer;
import com.ultimatesoftware.banking.tests.models.TransactionDto;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class TransactionE2ETest {

    private static String transactionHost;
    private static String accountQueryHost;
    private static String accountCmdHost;
    private static String customerHost;

    private Customer customer;
    private Account account1;
    private Account account2;

    @BeforeClass
    public static void setupOnce() {
        transactionHost = System.getenv("TRANSACTION_HOST") != null ?
                System.getenv("TRANSACTION_HOST") : "http://localhost:8086";
        accountQueryHost = System.getenv("ACCOUNT_QUERY_HOST") != null ?
                System.getenv("ACCOUNT_QUERY_HOST") : "http://localhost:8084";
        accountCmdHost = System.getenv("ACCOUNT_CMD_HOST") != null ?
                System.getenv("ACCOUNT_CMD_HOST") : "http://localhost:8089";
        customerHost = System.getenv("CUSTOMER_HOST") != null ?
                System.getenv("CUSTOMER_HOST") : "http://localhost:8082";
    }

    @Before
    public void setup() {
        // Create customer
        this.customer = new Customer("Jane", "Doe");
        RequestSpecification request = given()
            .body(customer).contentType("application/json");

        Response response = request.post(customerHost + "/api/v1/customers");
        response.then().statusCode(200);
        this.customer.setId(UUID.fromString(response.getBody().asString()));

        // Create accounts
        AccountCreationDto accountToCreate = new AccountCreationDto(this.customer.getId());
        request = given().body(accountToCreate).contentType("application/json");
        response = request.when()
            .post(accountCmdHost + "/api/v1/accounts");
        response.then().statusCode(200);
        this.account1 = new Account();
        this.account1.setId(UUID.fromString(response.getBody().asString()));

        accountToCreate = new AccountCreationDto(this.customer.getId());
        request = given().body(accountToCreate).contentType("application/json");
        response = request.when()
            .post(accountCmdHost + "/api/v1/accounts");
        response.then().statusCode(200);
        this.account2 = new Account();
        this.account2.setId(UUID.fromString(response.getBody().asString()));

        // deposit initial balance
        TransactionDto transactionDto = new TransactionDto(customer.getId(), this.account1.getId(), 15.00);
        request = given().body(transactionDto);
        response = request.when().put(accountCmdHost + "/api/v1/accounts/credit");
        response.then().statusCode(200);
    }

    @Test
    public void givenAccountsWithBalance_whenTransferingValidAmount_AmountIsTransfered() {
        // Arrange
        double amount = 10.00;
        RequestSpecification request = given().
            headers(
                "amount", amount,
                "accountId", this.account1.getId(),
                "destinationAccountId", this.account2.getId(),
                "customerId", this.customer.getId());
        // Act
        Response response = request.when().
            post(this.transactionHost + "/api/v1/transfer");

        // Assert
        ResponseBody body = response.getBody();
        response.then().statusCode(200);
        accountBalanceIs(this.account1.getId(), 10.00, 5);
        accountBalanceIs(this.account2.getId(), 5.00, 5);
        transactionStatusIsSuccessful("test", 5);
    }

    private void accountBalanceIs(UUID accountId, double balance, long seconds) {
        long secondsTaken = seconds;
        while (secondsTaken < seconds) {
            long startTime = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(1);
                when().
                    get(this.accountQueryHost + String.format("/api/v1/accounts/%s", accountId)).
                    then().
                    body("account.balance", equalTo(balance));
                return;
            } catch (Exception e) {}
            long endTime = System.currentTimeMillis();
            secondsTaken += (endTime - startTime) / 1000;
        }
    }

    private void transactionStatusIsSuccessful(String transactionId, long seconds) {
        long secondsTaken = seconds;
        while (secondsTaken < seconds) {
            long startTime = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(1);
                when().
                        get(this.accountQueryHost + String.format("/api/v1/transaction/%s", transactionId)).
                        then().
                        body("transaction.status", equalTo("SUCCESSFUL"));
                return;
            } catch (Exception e) {}
            long endTime = System.currentTimeMillis();
            secondsTaken += (endTime - startTime) / 1000;
        }
    }
}
