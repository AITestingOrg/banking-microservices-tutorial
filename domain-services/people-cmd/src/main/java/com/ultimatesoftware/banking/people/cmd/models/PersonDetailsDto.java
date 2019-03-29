package com.ultimatesoftware.banking.people.cmd.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDetailsDto {
    @NotBlank
    String id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
