package com.ultimatesoftware.banking.account.cmd.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    @NotNull
    private String id;
    @NotNull
    private UUID account;
    @NotNull
    private String customerId;
    @Min(1)
    private double amount;
    private UUID destinationAccount;

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id=" + id +
                ", account=" + account +
                ", customerId=" + customerId +
                ", amount=" + amount +
                ", destinationAccount=" + destinationAccount +
                '}';
    }
}
