package com.ultimatesoftware.banking.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = AccountCreditedEvent.AccountCreditedEventBuilder.class)
public class AccountCreditedEvent extends AccountTransactionEvent {
    private double balance;
    private double creditAmount;
    private String customerId;

    @Builder
    protected AccountCreditedEvent(String id, String customerId, double creditAmount, double balance, String transactionId) {
        super(id, transactionId);
        this.balance = balance;
        this.creditAmount = creditAmount;
        this.customerId = customerId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccountCreditedEventBuilder { }
}
