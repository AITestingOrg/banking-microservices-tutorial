package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransferTransactionStartedEvent.TransferTransactionStartedEventBuilder.class)
public class TransferWithdrawConcludedEvent extends AccountTransactionEvent {
    private double amount;
    private double balance;

    @Builder
    protected TransferWithdrawConcludedEvent(String id, double amount, double balance, String transactionId) {
        super(id, transactionId);
        this.amount = amount;
        this.balance = balance;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferWithdrawConcludedEventBuilder {}
}
