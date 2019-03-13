package com.ultimatesoftware.banking.events;

public abstract class AccountEvent {
    protected String id;

    public AccountEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
