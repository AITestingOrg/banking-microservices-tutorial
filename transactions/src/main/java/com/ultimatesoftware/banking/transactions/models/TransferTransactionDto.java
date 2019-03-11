package com.ultimatesoftware.banking.transactions.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransactionDto extends TransactionDto {
    private UUID destinationAccountId;

    @Builder
    public TransferTransactionDto(String customerId, UUID accountId, Double amount,
        UUID destinationAccountId) {
        super(customerId, accountId, amount);
        this.destinationAccountId = destinationAccountId;
    }

    public static class TransferTransactionDtoBuilder extends TransactionDto.TransactionDtoBuilder {
        TransferTransactionDtoBuilder() {
            super();
        }
    }
}
