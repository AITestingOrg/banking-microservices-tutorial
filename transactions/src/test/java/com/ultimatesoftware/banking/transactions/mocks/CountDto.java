package com.ultimatesoftware.banking.transactions.mocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountDto {
    private int count;

    @JsonCreator
    public CountDto(@JsonProperty("count") int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
