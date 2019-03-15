package com.ultimatesoftware.banking.events;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public abstract class AccountEvent {
    protected String id;

    public AccountEvent(String id) {
        this.id = id;
    }
}
