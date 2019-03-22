package com.ultimatesoftware.banking.people.events;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordResetEvent extends PersonEvent {
    private String password;
    public PasswordResetEvent(String id, @NotBlank String password) {
        super(id);
        this.password = password;
    }
}
