package com.ultimatesoftware.banking.people.cmd.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ultimatesoftware.banking.api.repository.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePersonDto extends Entity {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
