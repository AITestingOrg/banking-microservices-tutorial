package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = AccountUpdatedEvent.AccountUpdatedEventBuilder.class)
public class AccountUpdatedEvent extends AccountEvent {
    private String customerId;

    @Builder
    protected AccountUpdatedEvent(String id, String customerId) {
        super(id);
        this.customerId = customerId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccountUpdatedEventBuilder {}
}
