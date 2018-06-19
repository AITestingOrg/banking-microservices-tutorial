package com.ultimatesoftware.banking.account.cmd.domain.commands;

import com.ultimatesoftware.banking.account.cmd.domain.models.AccountUpdateDto;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class UpdateAccountCommand implements Command {
    @TargetAggregateIdentifier
    private UUID id;
    private UUID customerId;

    public UpdateAccountCommand(UUID id, UUID customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public UpdateAccountCommand(UUID id, AccountUpdateDto accountUpdateDto) {
        this.id = id;
        this.customerId = accountUpdateDto.getCustomerId();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
