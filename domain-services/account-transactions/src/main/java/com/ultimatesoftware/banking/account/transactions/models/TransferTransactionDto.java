package com.ultimatesoftware.banking.account.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = TransferTransactionDto.TransferTransactionDtoBuilder.class)
public class TransferTransactionDto extends TransactionDto {
    private String destinationAccountId;

    @Builder
    public TransferTransactionDto(String customerId, String accountId, Double amount,
        String destinationAccountId) {
        super(customerId, accountId, amount);
        this.destinationAccountId = destinationAccountId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransferTransactionDtoBuilder {}
}
