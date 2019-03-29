package com.ultimatesoftware.banking.people.cmd.commands;

import com.ultimatesoftware.banking.people.cmd.models.PersonDetailsDto;
import lombok.Getter;
import org.bson.types.ObjectId;

import javax.validation.Valid;

@Getter
public class UpdateDetailsCommand extends Command {
    private PersonDetailsDto personDetailsDto;

    public UpdateDetailsCommand(@Valid PersonDetailsDto personDetailsDto) {
        super(new ObjectId(personDetailsDto.getId()));
        this.personDetailsDto = personDetailsDto;
    }
}
