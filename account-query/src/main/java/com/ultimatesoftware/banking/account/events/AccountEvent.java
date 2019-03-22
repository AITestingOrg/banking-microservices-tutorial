package com.ultimatesoftware.banking.account.events;

import lombok.Getter;

@Getter
public abstract class AccountEvent {
    protected String id;

    public AccountEvent(String id) {
        this.id = id;
    }
}
