package com.ultimatesoftware.banking.transactions.tests.contracts.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "AccountQuery", port = "8084")
public class ConsumeSingleAccount {
    @Pact(provider = "AccountQuery", consumer = "Transactions")
    public RequestResponsePact createGetOnePact(PactDslWithProvider builder) {
        return builder
            .given("An account exists.")
            .uponReceiving("Request for single account")
            .path("/api/v1/accounts/5c892dbef72465ad7e7dde42")
            .method("GET")
            .headers(getHeaders())
            .willRespondWith()
            .status(200)
            .body("{\"id\":\"5c892dbef72465ad7e7dde42\",\"customerId\":\"5c86d04877970c1fd879a36b\",\"balance\":10.0}")
            .toPact();
    }

    @Test
    void testGetOne(MockServer mockServer) throws IOException {
        given()
            .headers(getHeaders()).
            when()
            .get(mockServer.getUrl() + "/api/v1/accounts/5c892dbef72465ad7e7dde42").
            then()
            .statusCode(200);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
