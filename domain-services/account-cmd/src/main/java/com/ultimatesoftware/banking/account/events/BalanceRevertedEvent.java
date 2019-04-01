package com.ultimatesoftware.banking.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Getter
@JsonDeserialize(builder = BalanceRevertedEvent.BalanceRevertedEventBuilder.class)
public class BalanceRevertedEvent extends AccountEvent {
    private final double balance;
    private final String transactionId;

    @Builder
    protected BalanceRevertedEvent(ObjectId id, BigDecimal balance, String transactionId) {
        super(id.toHexString());
        this.balance = balance.doubleValue();
        this.transactionId = transactionId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class BalanceRevertedEventBuilder {}
}
