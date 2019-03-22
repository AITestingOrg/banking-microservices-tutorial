package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;

@JsonDeserialize(builder = AccountDeletedEvent.AccountDeletedEventBuilder.class)
public class AccountDeletedEvent extends AccountEvent {
    @Builder
    protected AccountDeletedEvent(String id) {
        super(id);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccountDeletedEventBuilder {}
}
