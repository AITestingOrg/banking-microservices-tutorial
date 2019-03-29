package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = AccountCreditedEvent.AccountCreditedEventBuilder.class)
public class AccountCreatedEvent extends AccountEvent {
    private String customerId;
    private double balance;

    @Builder
    protected AccountCreatedEvent(String id, String customerId, double balance) {
        super(id);
        this.customerId = customerId;
        this.balance = balance;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccountCreatedEventBuilder {}
}
