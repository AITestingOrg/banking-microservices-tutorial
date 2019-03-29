package com.ultimatesoftware.banking.people.authentication.models;

import com.ultimatesoftware.banking.api.repository.Entity;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class Authentication extends Entity {
    private String email;
    private String password;

    public Authentication(ObjectId id, String email, String password) {
        super(id);
        this.email = email;
        this.password = password;
    }
}
