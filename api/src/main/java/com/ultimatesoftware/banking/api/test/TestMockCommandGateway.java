package com.ultimatesoftware.banking.api.test;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.Registration;
import org.axonframework.messaging.MessageDispatchInterceptor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TestMockCommandGateway implements CommandGateway {
    @Override public <C, R> void send(C command, CommandCallback<? super C, ? super R> callback) {

    }

    @Override public <R> R sendAndWait(Object command) {
        return null;
    }

    @Override public <R> R sendAndWait(Object command, long timeout, TimeUnit unit) {
        return null;
    }

    @Override public <R> CompletableFuture<R> send(Object command) {
        return null;
    }

    @Override public Registration registerDispatchInterceptor(
        MessageDispatchInterceptor<? super CommandMessage<?>> dispatchInterceptor) {
        return null;
    }
}
