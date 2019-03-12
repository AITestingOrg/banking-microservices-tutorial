package com.ultimatesoftware.banking.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.ultimatesoftware.banking.api.repository.Entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends Entity {
    private TransactionType type;
    private String accountId;
    private String customerId;
    private double amount;
    private String destinationAccount;
    private @Setter TransactionStatus status = TransactionStatus.IN_PROGRESS;

    @Builder
    public Transaction(ObjectId id,
        TransactionType type, String accountId, String customerId, double amount,
        String destinationAccount,
        TransactionStatus status) {
        super(id);
        this.type = type;
        this.accountId = accountId;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
        this.status = status;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionBuilder {
    }
}
