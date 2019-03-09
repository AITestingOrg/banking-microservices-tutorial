package com.ultimatesoftware.banking.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    private String customerId;
    private UUID accountId;
    private Double amount;

    public TransactionDto(String customerId, UUID accountId, Double amount) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public Double getAmount() {
        return amount;
    }
}
