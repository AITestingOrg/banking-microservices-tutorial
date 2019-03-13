package com.ultimatesoftware.banking;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.inject.Singleton;

@OpenAPIDefinition(
    info = @Info(
        title = "Account-Cmd",
        version = "0.1",
        description = "Account Command Service",
        license = @License(name = "Apache 2.0", url = "http://ultimatesoftware.com")
    ),
    tags = {
        @Tag(name = "Account")
    }
)
@Singleton
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}