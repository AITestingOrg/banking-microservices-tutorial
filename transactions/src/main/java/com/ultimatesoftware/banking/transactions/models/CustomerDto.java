package com.ultimatesoftware.banking.transactions.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {
    private String id;
    private String firstName;
    private String lastName;

    @JsonCreator
    public CustomerDto(@JsonProperty("id") String id, @JsonProperty("firstName")  String firstName, @JsonProperty("lastName")  String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}


