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
@PactTestFor(providerName = "Customers", port = "8085")
public class ConsumeSingleCustomer {
    @Pact(provider = "Customers", consumer = "Transactions")
    public RequestResponsePact createGetOnePact(PactDslWithProvider builder) {
        return builder
            .given("A customer exist.")
            .uponReceiving("Single valid customer")
            .path("/api/v1/customers/5c892dbef72465ad7e7dde42")
            .method("GET")
            .headers(getHeaders())
            .willRespondWith()
            .status(200)
            .body("{\"id\":\"5c892dbef72465ad7e7dde42\",\"firstName\":\"Samantha\",\"lastName\":\"Carter\"}")
            .toPact();
    }

    @Test
    void testGetOne(MockServer mockServer) throws IOException {
        given()
            .headers(getHeaders()).
            when()
            .get(mockServer.getUrl() + "/api/v1/customers/5c892dbef72465ad7e7dde42").
            then()
            .statusCode(200);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
