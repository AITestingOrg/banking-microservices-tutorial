package com.ultimatesoftware.banking.account.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.ultimatesoftware.banking.api.repository.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends Entity {
    private TransactionType type;
    private String accountId;
    private String customerId;
    private double amount;
    private String destinationAccountId;
    private @Setter TransactionStatus status = TransactionStatus.IN_PROGRESS;

    @BsonCreator
    @Builder
    public Transaction(
        @BsonProperty("id") ObjectId id,
        @BsonProperty("type") TransactionType type,
        @BsonProperty("accountId") String accountId,
        @BsonProperty("customerId") String customerId,
        @BsonProperty("amount") double amount,
        @BsonProperty("destinationAccount") String destinationAccountId,
        @BsonProperty("status") TransactionStatus status) {
        super(id);
        this.type = type;
        this.accountId = accountId;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccountId = destinationAccountId;
        if (status != null) {
            this.status = status;
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionBuilder {
    }
}
