package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransferWithdrawConcludedEvent.TransferWithdrawConcludedEventBuilder.class)
public class TransferWithdrawConcludedEvent extends AccountTransactionEvent {
    private double balance;

    @Builder
    protected TransferWithdrawConcludedEvent(String id, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferWithdrawConcludedEventBuilder {}
}
