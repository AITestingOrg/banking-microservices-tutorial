package com.ultimatesoftware.banking.people.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = PasswordResetEvent.PasswordResetEventBuilder.class)
public class PasswordResetEvent extends PersonEvent {
    private String email;
    private String password;

    @Builder
    protected PasswordResetEvent(String id, String email, String password) {
        super(id);
        this.email = email;
        this.password = password;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class PasswordResetEventBuilder {}
}
