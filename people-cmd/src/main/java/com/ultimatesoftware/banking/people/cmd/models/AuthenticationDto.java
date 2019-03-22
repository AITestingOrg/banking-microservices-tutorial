package com.ultimatesoftware.banking.people.cmd.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationDto {
    @NotBlank
    private String id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
