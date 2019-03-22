package com.ultimatesoftware.banking.account.transactions.tests.contracts.consumer;

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
@PactTestFor(providerName = "PeopleDetails", port = "8085")
public class ConsumeManyPersonDetails {
    @Pact(provider = "PeopleDetails", consumer = "AccountTransactions")
    public RequestResponsePact createGetManyPact(PactDslWithProvider builder) {
        return builder
            .given("Three person exist.")
            .uponReceiving("Three PersonDetails")
            .path("/api/v1/people/")
            .method("GET")
            .headers(getHeaders())
            .willRespondWith()
            .status(200)
            .body("[{\"id\":\"5c86d04877970c1fd879a36b\",\"firstName\":\"Jack\",\"lastName\":\"Oneill\"},{\"id\":\"5c892dbef72465ad7e7dde42\",\"firstName\":\"Samantha\",\"lastName\":\"Carter\"},{\"id\":\"5c89342ef72465c5981bc1fc\",\"firstName\":\"Daniel\",\"lastName\":\"Jackson\"}]")
            .toPact();
    }

    @Test
    void testGetMany(MockServer mockServer) throws IOException {
        given()
            .headers(getHeaders()).
            when()
            .get(mockServer.getUrl() + "/api/v1/people/").
            then()
            .statusCode(200);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
