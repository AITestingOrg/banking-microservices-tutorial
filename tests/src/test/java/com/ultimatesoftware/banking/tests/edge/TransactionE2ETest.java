package com.ultimatesoftware.banking.tests.edge;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class TransactionE2ETest {

    private String transactionHost;
    private String accountQueryHost;

    @Before
    public void setup() {
        this.transactionHost = System.getenv("TRANSACTION_HOST") != null ?
                System.getenv("TRANSACTION_HOST") : "http://localhost:8086";
        this.accountQueryHost = System.getenv("ACCOUNT_QUERY_HOST") != null ?
                System.getenv("ACCOUNT_QUERY_HOST") : "http://localhost:8084";
    }

    @Test
    public void givenAccountsWithBalance_whenTransferingValidAmount_AmountIsTransfered() {
        // Arrange
        RestAssured.baseURI = this.transactionHost;
        double amount = 10.00;
        String account1 = "";
        String account2 = "";
        String customerId = "";
        RequestSpecification request = given().
            headers(
                "amount", amount,
                "accountId", account1,
                "destinationAccountId", account2,
                "customerId", customerId);
        // Act
        Response response = request.when().
            post(this.transactionHost + "/api/transfer");

        // Assert
        ResponseBody body = response.getBody();
        response.then().statusCode(200);
        accountBalanceIs(account1, 10.00, 5);
        accountBalanceIs(account2, 5.00, 5);
        transactionStatusIsSuccessful("test", 5);
    }

    private void accountBalanceIs(String accountId, double balance, long seconds) {
        long secondsTaken = seconds;
        while(secondsTaken < seconds) {
            long startTime = System.currentTimeMillis();
            try {
                when().
                    get(this.accountQueryHost + String.format("/api/account/%s", accountId)).
                    then().
                    body("account.balance", equalTo(balance));
                secondsTaken = Long.MAX_VALUE;
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {}
            long endTime = System.currentTimeMillis();
            secondsTaken += (endTime - startTime) / 1000;
        }
    }

    private void transactionStatusIsSuccessful(String transactionId, long seconds) {
        long secondsTaken = seconds;
        while(secondsTaken < seconds) {
            long startTime = System.currentTimeMillis();
            try {
                when().
                        get(this.accountQueryHost + String.format("/api/transaction/%s", transactionId)).
                        then().
                        body("transaction.status", equalTo("SUCCESSFUL"));
                secondsTaken = Long.MAX_VALUE;
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {}
            long endTime = System.currentTimeMillis();
            secondsTaken += (endTime - startTime) / 1000;
        }
    }
}
