package com.ultimatesoftware.banking.account.cmd.exceptions;

public class PersonNotFoundException extends Exception {
    private String personId;

    public PersonNotFoundException(String personId, String msg) {
        super(msg);
        this.personId = personId;
    }

    public String getPersonId() {
        return personId;
    }
}
