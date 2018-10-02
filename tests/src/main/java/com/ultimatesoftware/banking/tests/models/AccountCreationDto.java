package com.ultimatesoftware.banking.tests.models;

public class AccountCreationDto {
    private String customerId;

    public AccountCreationDto(String customerId) {
        this.customerId = customerId;
    }

    public AccountCreationDto() {

    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AccountCreationDto{" +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
