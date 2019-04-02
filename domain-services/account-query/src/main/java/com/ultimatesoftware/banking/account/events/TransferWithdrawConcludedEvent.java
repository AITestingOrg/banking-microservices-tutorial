package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TransferWithdrawConcludedEvent.TransferWithdrawConcludedEventBuilder.class)
public class TransferWithdrawConcludedEvent extends AccountTransactionEvent {
    private double balance;
    private double amount;

    @Builder
    protected TransferWithdrawConcludedEvent(String id, double amount, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
        this.amount = amount;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferWithdrawConcludedEventBuilder {}
}
