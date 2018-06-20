package com.ultimatesoftware.banking.eventsourcing.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public abstract class CustomSaga implements Serializable {
    protected transient CommandGateway commandGateway;

    @Autowired
    public void setCommandGateway(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
