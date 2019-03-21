package com.ultimatesoftware.banking.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransferCanceledEvent.TransferCanceledEventBuilder.class)
public class TransferCanceledEvent extends AccountTransactionEvent {
    private double balance;

    @Builder
    protected TransferCanceledEvent(String id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferCanceledEventBuilder {}
}
