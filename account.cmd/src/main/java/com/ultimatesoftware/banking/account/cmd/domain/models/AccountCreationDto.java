package com.ultimatesoftware.banking.account.cmd.domain.models;

import javax.validation.constraints.Min;

public class AccountCreationDto {
    @Min(18)
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
