package com.ultimatesoftware.banking.account.query.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ultimatesoftware.banking.api.repository.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account extends Entity {
    @NotNull
    @NotBlank
    private String customerId;
    private double balance;

    @BsonCreator
    @JsonCreator
    public Account(@BsonProperty("id") @JsonProperty("id") ObjectId id,
        @BsonProperty("customerId") @JsonProperty("customerId") String customerId,
        @BsonProperty("balance") @JsonProperty("balance") double balance) {
        super(id);
        this.customerId = customerId;
        this.balance = balance;
    }
}
