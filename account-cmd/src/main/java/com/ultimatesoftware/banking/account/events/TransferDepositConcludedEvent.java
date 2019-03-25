package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransferDepositConcludedEvent.TransferDepositConcludedEventBuilder.class)
public class TransferDepositConcludedEvent extends AccountTransactionEvent {
    private double balance;

    @Builder
    protected TransferDepositConcludedEvent(String id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferDepositConcludedEventBuilder {}
}
