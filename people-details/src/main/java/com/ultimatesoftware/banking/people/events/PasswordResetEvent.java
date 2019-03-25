package com.ultimatesoftware.banking.people.events;

import lombok.Getter;

@Getter
public class PasswordResetEvent extends PersonEvent {
    private String password;
    public PasswordResetEvent(String id, String password) {
        super(id);
        this.password = password;
    }
}
