package com.ultimatesoftware.banking.people.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
@JsonDeserialize(builder = PersonCreatedEvent.PersonCreatedEventBuilder.class)
public class PersonCreatedEvent extends PersonEvent {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Builder
    protected PersonCreatedEvent(ObjectId id, String email, String password, String firstName, String lastName) {
        super(id.toHexString());
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class PersonCreatedEventBuilder {}
}
