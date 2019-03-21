package com.ultimatesoftware.banking.people.cmd.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
