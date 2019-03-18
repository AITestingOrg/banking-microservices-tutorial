package com.ultimatesoftware.banking.transactions.tests.mocks;

import org.bson.types.ObjectId;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class CustomersMock extends HttpMock {
    public CustomersMock() {
        super(8085);
    }

    public void onCustomerGet_Return404(ObjectId customerId) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/customers/" + customerId.toHexString()))
            .willReturn(aResponse()
                .withStatus(404)));
    }

    public void onCustomerGet_ReturnCustomer(ObjectId customerId, String firstName, String lastName) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/customers/" + customerId.toHexString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(String.format("{"
                    + "    \"id\": \"%s\","
                    + "    \"firstName\": \"%s\","
                    + "    \"lastName\": %s"
                    + "  }", customerId.toHexString(), firstName, lastName))));
    }
}
