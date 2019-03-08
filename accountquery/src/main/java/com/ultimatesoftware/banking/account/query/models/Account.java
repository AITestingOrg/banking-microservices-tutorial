package com.ultimatesoftware.banking.account.query.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ultimatesoftware.banking.api.repository.Entity;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account extends Entity {
    @NotNull
    private UUID accountId;
    @NotNull
    private String customerId;
    private double balance;

    @BsonCreator
    @JsonCreator
    public Account(@BsonProperty("id") @JsonProperty("id") String id, @BsonProperty("accountId") @JsonProperty("accountId") UUID accountId,
        @BsonProperty("customerId") @JsonProperty("customerId") String customerId, @BsonProperty("balance") @JsonProperty("balance") double balance) {
        super(id);
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
    }

    public Account(UUID accountId, String customerId, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
    }

    public Account() {
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
