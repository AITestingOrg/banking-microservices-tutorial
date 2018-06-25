package com.ultimatesoftware.banking.authorization.service.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Document
public class User {

    @Id
    private UUID id;
    @NotNull(message = "userName field is required.")
    private String userName;
    @NotNull(message = "password field if required.")
    private String password;
    private String role;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.id = UUID.randomUUID();
        this.role = "user";
    }

    public User() {}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public UUID getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
