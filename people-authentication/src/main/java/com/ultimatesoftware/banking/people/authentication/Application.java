package com.ultimatesoftware.banking.people.authentication;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
    info = @Info(
        title = "People.Authentication",
        version = "0.1",
        description = "Authentication Service",
        license = @License(name = "Apache 2.0", url = "http://ultimatesoftware.com")
    ),
    tags = {
        @Tag(name = "People"),
        @Tag(name = "Authentication")
    }
)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
