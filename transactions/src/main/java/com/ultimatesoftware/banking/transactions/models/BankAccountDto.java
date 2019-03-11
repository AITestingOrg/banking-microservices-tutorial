package com.ultimatesoftware.banking.transactions.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDto {
    private UUID id;
    private double balance;
    private String customerId;
}
