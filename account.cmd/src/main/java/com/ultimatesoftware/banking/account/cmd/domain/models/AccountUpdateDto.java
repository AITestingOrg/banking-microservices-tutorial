package com.ultimatesoftware.banking.account.cmd.domain.models;

public class AccountUpdateDto {
    private String customerId;

    public AccountUpdateDto(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AccountUpdateDto{" +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
