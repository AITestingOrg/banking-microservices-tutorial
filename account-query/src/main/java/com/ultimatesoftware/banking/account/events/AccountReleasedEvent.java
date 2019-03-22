package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;

@JsonDeserialize(builder = AccountReleasedEvent.AccountReleasedEventBuilder.class)
public class AccountReleasedEvent extends AccountTransactionEvent {
    @Builder
    protected AccountReleasedEvent(String id, String transactionId) {
        super(id, transactionId);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccountReleasedEventBuilder {}
}
