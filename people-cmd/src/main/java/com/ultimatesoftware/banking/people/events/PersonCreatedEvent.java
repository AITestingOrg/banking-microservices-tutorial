package com.ultimatesoftware.banking.people.events;

import com.ultimatesoftware.banking.people.cmd.models.CreatePersonDto;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class PersonCreatedEvent extends PersonEvent {
    private CreatePersonDto person;
    public PersonCreatedEvent(ObjectId id, CreatePersonDto person) {
        super(id.toHexString());
    }
}
