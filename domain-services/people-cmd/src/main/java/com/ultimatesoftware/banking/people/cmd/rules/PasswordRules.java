package com.ultimatesoftware.banking.people.cmd.rules;

import com.ultimatesoftware.banking.people.cmd.exceptions.InvalidPasswordException;

public interface PasswordRules {
    void passwordMeetsRules(String password) throws InvalidPasswordException;
}
