package com.ultimatesoftware.banking.customerquery.domain.eventhandlers;

import com.ultimatesoftware.banking.customerquery.domain.events.CustomerCreatedEvent;
import com.ultimatesoftware.banking.customerquery.domain.events.CustomerDeletedEvent;
import com.ultimatesoftware.banking.customerquery.domain.events.CustomerUpdatedEvent;
import com.ultimatesoftware.banking.customerquery.service.repositories.CustomerRepository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(CustomerEventHandler.class);

    @Autowired
    CustomerRepository customerRepository;

    @EventHandler
    public void handle(CustomerCreatedEvent event) {
        LOG.info(String.format("Customer Created: %s %s ID: %s", event.getFirstName(), event.getLastName(), event.getId()));
    }

    @EventHandler
    public void handle(CustomerUpdatedEvent event) {
        LOG.info(String.format("Customer Updated: %s %s ID: %s", event.getFirstName(), event.getLastName(), event.getId()));
    }

    @EventHandler
    public void handle(CustomerDeletedEvent event) {
        LOG.info(String.format("Customer Deleted %s", event.getId()));
    }
}
