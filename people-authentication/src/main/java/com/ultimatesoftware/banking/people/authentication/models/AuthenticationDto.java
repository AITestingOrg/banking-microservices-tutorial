package com.ultimatesoftware.banking.people.authentication.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
