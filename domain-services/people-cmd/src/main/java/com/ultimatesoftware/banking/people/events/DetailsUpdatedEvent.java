package com.ultimatesoftware.banking.people.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = DetailsUpdatedEvent.DetailsUpdatedEventBuilder.class)
public class DetailsUpdatedEvent extends PersonEvent {
    private String firstName;
    private String lastName;

    @Builder
    protected DetailsUpdatedEvent(String id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class DetailsUpdatedEventBuilder {}
}
