package com.ultimatesoftware.banking.customers.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ultimatesoftware.banking.api.repository.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends Entity {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @BsonCreator
    @JsonCreator
    public Customer(@BsonProperty("id")@JsonProperty("id") String id, @BsonProperty("firstName")@JsonProperty("firstName") String firstName, @BsonProperty("lastName")@JsonProperty("lastName") String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer() {
    }

    @NotBlank
    public String getFirstName() {
        return firstName;
    }

    @NotBlank
    public String getLastName() {
        return lastName;
    }
}
