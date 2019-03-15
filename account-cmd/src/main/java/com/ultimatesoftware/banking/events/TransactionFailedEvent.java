package com.ultimatesoftware.banking.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransactionFailedEvent.TransactionFailedEventBuilder.class)
public class TransactionFailedEvent extends AccountTransactionEvent {
    private String msg;

    @Builder
    protected TransactionFailedEvent(String id, String transactionId, String msg) {
        super(id, transactionId);
        this.msg = msg;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionFailedEventBuilder {}
}
