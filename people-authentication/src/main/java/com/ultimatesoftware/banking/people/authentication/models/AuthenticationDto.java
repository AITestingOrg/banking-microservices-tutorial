package com.ultimatesoftware.banking.people.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class AuthenticationDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
