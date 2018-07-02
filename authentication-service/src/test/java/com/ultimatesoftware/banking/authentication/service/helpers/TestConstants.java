package com.ultimatesoftware.banking.authentication.service.helpers;

import com.ultimatesoftware.banking.authentication.service.model.User;

import java.util.UUID;

public class TestConstants {
    public static final UUID T_ID = UUID.fromString("4081c87d-64fc-4022-9011-232476ccccfb");
    public static final String T_USERNAME = "JohnDoe";
    public static final String T_PASSWORD = "password";
    public static final User T_USER = new User(T_USERNAME, T_PASSWORD);
}
