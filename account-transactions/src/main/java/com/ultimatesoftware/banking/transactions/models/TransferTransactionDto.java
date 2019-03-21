package com.ultimatesoftware.banking.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferTransactionDto extends TransactionDto {
    private String destinationAccountId;

    @Builder
    public TransferTransactionDto(String customerId, String accountId, Double amount,
        String destinationAccountId) {
        super(customerId, accountId, amount);
        this.destinationAccountId = destinationAccountId;
    }
}
