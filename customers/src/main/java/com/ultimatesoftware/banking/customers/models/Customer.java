package com.ultimatesoftware.banking.customers.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ultimatesoftware.banking.api.repository.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends Entity {
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;

    @BsonCreator
    @JsonCreator
    public Customer(@BsonProperty("id") @JsonProperty("id") ObjectId id,
        @BsonProperty("firstName") @JsonProperty("firstName") String firstName,
        @BsonProperty("lastName") @JsonProperty("lastName")  String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
