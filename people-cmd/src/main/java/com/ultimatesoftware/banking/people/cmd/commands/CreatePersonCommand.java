package com.ultimatesoftware.banking.people.cmd.commands;

import com.ultimatesoftware.banking.people.cmd.models.CreatePersonDto;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

@Getter
public class CreatePersonCommand {
    @TargetAggregateIdentifier
    private ObjectId id;
    private CreatePersonDto person;

    public CreatePersonCommand(CreatePersonDto person) {
        this.id = ObjectId.get();
        this.person = person;
    }
}
