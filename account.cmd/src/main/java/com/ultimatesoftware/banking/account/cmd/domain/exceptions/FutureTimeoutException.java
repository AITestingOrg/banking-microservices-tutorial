package com.ultimatesoftware.banking.account.cmd.domain.exceptions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

public class FutureTimeoutException implements Runnable {
    private CompletableFuture future;

    public FutureTimeoutException(CompletableFuture future) {
        this.future = future;
    }

    @Override
    public void run() {
        future.completeExceptionally(new TimeoutException());
    }
}
