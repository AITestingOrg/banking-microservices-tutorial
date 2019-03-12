package com.ultimatesoftware.banking.transactions.configuration;

import io.micronaut.context.annotation.Value;

public class TransactionConfiguration {
    @Value("${micronaut.application.name}")
    private String databaseName;
}
