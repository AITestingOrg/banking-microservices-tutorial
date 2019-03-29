package com.ultimatesoftware.banking.account.transactions.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDto {
    private String id;
    private double balance;
    private String customerId;

    @JsonCreator
    public BankAccountDto(@JsonProperty("id") String id, @JsonProperty("balance") double balance, @JsonProperty("customerId")  String customerId) {
        this.id = id;
        this.balance = balance;
        this.customerId = customerId;
    }
}
