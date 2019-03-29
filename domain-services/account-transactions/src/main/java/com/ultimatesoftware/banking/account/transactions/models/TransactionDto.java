package com.ultimatesoftware.banking.account.transactions.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    @NotBlank
    @NotNull
    private String customerId;
    @NotBlank
    @NotNull
    private String accountId;
    @Min(value = 0)
    private Double amount;

    @JsonCreator
    public TransactionDto(@JsonProperty("customerId") String customerId, @JsonProperty("accountId") String accountId, @JsonProperty("amount") Double amount) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.amount = amount;
    }
}
