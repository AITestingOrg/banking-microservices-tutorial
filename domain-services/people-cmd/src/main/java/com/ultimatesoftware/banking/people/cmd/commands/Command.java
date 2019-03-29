package com.ultimatesoftware.banking.people.cmd.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
@AllArgsConstructor
public abstract class Command {
    @TargetAggregateIdentifier
    private ObjectId id;
}
