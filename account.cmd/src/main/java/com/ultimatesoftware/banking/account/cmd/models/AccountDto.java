package com.ultimatesoftware.banking.account.cmd.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    @NotNull
    @NotBlank
    private String customerId;

    @Override
    public String toString() {
        return "AccountCreationDto{" +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
