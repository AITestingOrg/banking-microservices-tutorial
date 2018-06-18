package com.ultimatesoftware.banking.account.cmd.domain.models;

import javax.validation.constraints.Min;
import java.util.UUID;

public class AccountCreationDto {
    @Min(18)
    private UUID customerId;

    public AccountCreationDto(UUID customerId) {
        this.customerId = customerId;
    }

    public AccountCreationDto() {

    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AccountCreationDto{" +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
