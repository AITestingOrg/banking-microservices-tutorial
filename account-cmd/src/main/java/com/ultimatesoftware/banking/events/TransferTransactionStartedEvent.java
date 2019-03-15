
package com.ultimatesoftware.banking.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransferTransactionStartedEvent.TransferTransactionStartedEventBuilder.class)
public class TransferTransactionStartedEvent extends AccountTransactionEvent {
    private String destinationAccountId;
    private double amount;

    @Builder
    protected TransferTransactionStartedEvent(String id, String destinationAccountId, double amount, String transactionId) {
        super(id, transactionId);
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferTransactionStartedEventBuilder {}
}
