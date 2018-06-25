package com.ultimatesoftware.banking.eventsourcing.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.Serializable;

public abstract class CustomSaga implements Serializable {
    protected transient CommandGateway commandGateway;
    protected transient ThreadPoolTaskScheduler executor;

    @Autowired
    public void setCommandGateway(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setExecutor(ThreadPoolTaskScheduler executor) {
        this.executor = executor;
    }
}
