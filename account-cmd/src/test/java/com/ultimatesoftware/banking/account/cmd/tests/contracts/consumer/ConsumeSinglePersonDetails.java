package com.ultimatesoftware.banking.account.cmd.tests.contracts.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "PeopleDetails", port = "8085")
public class ConsumeSinglePersonDetails {
    @Pact(provider = "PeopleDetails", consumer = "AccountCmd")
    public RequestResponsePact createGetOnePact(PactDslWithProvider builder) {
        return builder
            .given("A person exist.")
            .uponReceiving("Single valid person")
            .path("/api/v1/people/5c892dbef72465ad7e7dde42")
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
            .get(mockServer.getUrl() + "/api/v1/people/5c892dbef72465ad7e7dde42").
            then()
            .statusCode(200);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
