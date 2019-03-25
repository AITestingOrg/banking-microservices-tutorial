package com.ultimatesoftware.banking.api.test;

public class ResponseDto {
    private String body;
    private int statusCode;

    public ResponseDto(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }
}