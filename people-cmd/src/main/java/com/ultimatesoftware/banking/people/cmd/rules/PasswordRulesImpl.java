package com.ultimatesoftware.banking.people.cmd.rules;

import com.ultimatesoftware.banking.people.cmd.exceptions.InvalidPasswordException;
import java.util.regex.Pattern;
import javax.inject.Singleton;

@Singleton
public class PasswordRulesImpl implements PasswordRules {
    private static final Pattern symbol = Pattern.compile("[A-Za-z0-9 ]*", Pattern.CASE_INSENSITIVE);
    private static final Pattern upperCase = Pattern.compile("(?s).*[A-Z].*");
    private static final Pattern lowerCase = Pattern.compile("(?s).*[a-z].*");

    @Override public void passwordMeetsRules(String password) throws InvalidPasswordException {
        if (password.length() < 8) {
            throw new InvalidPasswordException("The password must be at least 8 characters.");
        } else if (!symbol.matcher(password).find()) {
            throw new InvalidPasswordException("The password must contain at least one non alpha-numeric character.");
        } else if(!upperCase.matcher(password).find()) {
            throw new InvalidPasswordException("The password must be at least one upper-case character.");
        } else if(!lowerCase.matcher(password).find()) {
            throw new InvalidPasswordException("The password must be at least one lower-case character.");
        }
    }
}
