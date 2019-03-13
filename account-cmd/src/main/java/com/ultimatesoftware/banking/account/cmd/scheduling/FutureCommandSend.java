package com.ultimatesoftware.banking.account.cmd.scheduling;

import com.ultimatesoftware.banking.account.cmd.commands.TransactionCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;

public class FutureCommandSend implements Runnable {
    private CommandGateway commandGateway;
    private TransactionCommand command;

    public FutureCommandSend(CommandGateway commandGateway, TransactionCommand command) {
        this.commandGateway = commandGateway;
        this.command = command;
    }

    @Override
    public void run() {
        commandGateway.send(command);
    }
}
