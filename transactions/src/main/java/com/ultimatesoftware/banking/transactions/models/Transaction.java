package com.ultimatesoftware.banking.transactions.models;

import com.ultimatesoftware.banking.api.repository.Entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends Entity {
    private TransactionType type;
    private UUID account;
    private String customerId;
    private double amount;
    private UUID destinationAccount;
    @Builder.Default
    private @Setter TransactionStatus status = TransactionStatus.IN_PROGRESS;
}
