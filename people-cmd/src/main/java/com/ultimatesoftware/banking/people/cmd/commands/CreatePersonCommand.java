package com.ultimatesoftware.banking.people.cmd.commands;

import com.ultimatesoftware.banking.people.cmd.models.CreatePersonDto;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class CreatePersonCommand extends Command {
    private CreatePersonDto person;

    public CreatePersonCommand(CreatePersonDto person) {
        super(ObjectId.get());
        this.person = person;
    }
}
