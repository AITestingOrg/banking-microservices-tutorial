package com.ultimatesoftware.banking.customers;

import groovy.lang.Singleton;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
    info = @Info(
        title = "Customers",
        version = "0.1",
        description = "Customer CRUD Service",
        license = @License(name = "Apache 2.0", url = "http://ultimatesoftware.com")
    ),
    tags = {
        @Tag(name = "Customers")
    }
)
@Singleton
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}