package com.ultimatesoftware.banking.account.transactions;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Singleton;

@OpenAPIDefinition(
    info = @Info(
        title = "Account Transactions",
        version = "0.1",
        description = "Account Transactions service",
        license = @License(name = "Apache 2.0", url = "http://ultimatesoftware.com")
    ),
    tags = {
        @Tag(name = "Transaction")
    }
)
@Singleton
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
