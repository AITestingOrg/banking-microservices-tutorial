package com.ultimatesoftware.banking.transactions.mocks;

import org.bson.types.ObjectId;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class AccountQueryMock extends HttpMock {
    public AccountQueryMock() {
        super(8084);
    }

    public void onAccountGet_Return404(ObjectId accountId) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/accounts/" + accountId.toHexString()))
            .willReturn(aResponse()
                .withStatus(404)));
    }

    public void onAccountGet_ReturnAccount_WithBalance(ObjectId accountId, ObjectId customerId, double balance) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/accounts/" + accountId.toHexString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(String.format("{"
                    + "    \"id\": \"%s\","
                    + "    \"customerId\": \"%s\","
                    + "    \"balance\": %.2f"
                    + "  }", accountId.toHexString(), customerId.toHexString(), balance))));
    }
}
