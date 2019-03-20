package com.ultimatesoftware.banking.transactions.tests.contracts.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultimatesoftware.banking.transactions.models.TransferTransactionDto;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "AccountCmd", port = "8082")
public class ConsumeTransferAccount {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Pact(provider="AccountCmd", consumer="Transactions")
    public RequestResponsePact createGetOnePact(PactDslWithProvider builder)
        throws JsonProcessingException {
        return builder
            .given("An account exists.")
            .uponReceiving("Request to transfer amount to destination account.")
            .path("/api/v1/accounts/transfer")
            .method("PUT")
            .headers(getHeaders())
            .body(objectMapper.writeValueAsString(new TransferTransactionDto("5c892dbef72465ad7e7dde42", "5c892dbef72465ad7e7dde42", 10.0, "5c892dbef72465ad7e7dde42")))
            .willRespondWith()
            .status(200)
            .body("SENT COMMAND")
            .toPact();
    }

    @Test
    void testGetOne(MockServer mockServer) throws IOException {
        given()
            .headers(getHeaders())
            .body(objectMapper.writeValueAsString(new TransferTransactionDto("5c892dbef72465ad7e7dde42", "5c892dbef72465ad7e7dde42", 10.0, "5c892dbef72465ad7e7dde42"))).
            when()
            .put(mockServer.getUrl() + "/api/v1/accounts/transfer").
            then()
            .statusCode(200);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
