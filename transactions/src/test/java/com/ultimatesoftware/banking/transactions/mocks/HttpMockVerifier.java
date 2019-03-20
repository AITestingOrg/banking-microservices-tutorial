package com.ultimatesoftware.banking.transactions.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.ultimatesoftware.banking.transactions.utils.StringTools.convertStreamToString;

public class HttpMockVerifier {
    private final HttpClient httpClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    public HttpMockVerifier(String host, int port) {
        this.httpClient = HttpClient.getBuilder()
            .setHost(host)
            .setPath("/__admin/requests/count")
            .setPort(port)
            .build();
    }

    public int verifyRequestCount(String jsonFile) throws Exception {
        String verification =  convertStreamToString(this.getClass().getResourceAsStream("/wiremock/" + jsonFile));
        verification = verification.replace("\n", "");
        ResponseDto response = httpClient.post(verification, "");
        if (response.getStatusCode() != 200) {
            throw new Exception(String.format("Expected count for Http mock, instead could not find stub based on %s criteria.", jsonFile));
        }
        CountDto count = objectMapper.readValue(response.getBody(), CountDto.class);
        return count.getCount();
    }
}
