package com.ultimatesoftware.banking.account.cmd.domain.commands;

import com.ultimatesoftware.banking.account.cmd.domain.models.AccountUpdateDto;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class UpdateAccountCommand {
    @TargetAggregateIdentifier
    private UUID id;
    private String customerId;
    private double balance;
    private boolean active;

    public UpdateAccountCommand(UUID id, String customerId, double balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
    }

    public UpdateAccountCommand(UUID id, AccountUpdateDto accountUpdateDto) {
        this.id = id;
        this.customerId = accountUpdateDto.getCustomerId();
        this.balance = accountUpdateDto.getBalance();
        this.active = accountUpdateDto.isActive();
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}
