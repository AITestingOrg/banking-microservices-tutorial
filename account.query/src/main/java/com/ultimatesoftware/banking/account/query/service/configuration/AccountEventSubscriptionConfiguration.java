package com.ultimatesoftware.banking.account.query.service.configuration;

import com.ultimatesoftware.banking.eventsourcing.configurations.AmqpEventSubscriptionConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountEventSubscriptionConfiguration extends AmqpEventSubscriptionConfiguration {}
