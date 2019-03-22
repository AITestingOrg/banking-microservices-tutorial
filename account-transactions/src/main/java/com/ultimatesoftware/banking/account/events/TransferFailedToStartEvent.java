package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;

@JsonDeserialize(builder = TransactionFailedEvent.TransactionFailedEventBuilder.class)
public class TransferFailedToStartEvent extends AccountTransactionEvent {
    @Builder
    protected TransferFailedToStartEvent(String id, String transactionId) {
        super(id, transactionId);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionFailedEventBuilder {}
}
