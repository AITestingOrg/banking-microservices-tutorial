package com.ultimatesoftware.banking.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = AccountDebitedEvent.AccountDebitedEventBuilder.class)
public class AccountDebitedEvent extends AccountTransactionEvent {
    private double balance;
    private double debitAmount;
    private String customerId;

    @Builder
    protected AccountDebitedEvent(String id, String customerId, double debitAmount, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
        this.debitAmount = debitAmount;
        this.customerId = customerId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccountDebitedEventBuilder {}
}
