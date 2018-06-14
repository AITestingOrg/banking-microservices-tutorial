package com.ultimatesoftware.banking.account.cmd.domain.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

public class TransactionAmount {
    @NotBlank
    @Min(1)
    private double amount;

    public TransactionAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
