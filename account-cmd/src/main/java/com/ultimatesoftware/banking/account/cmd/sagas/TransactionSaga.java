package com.ultimatesoftware.banking.account.cmd.sagas;

import com.ultimatesoftware.banking.account.cmd.commands.*;
import com.ultimatesoftware.banking.account.events.*;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionSaga {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSaga.class);

    private String transactionId;
    private String sourceAccountId;
    private String destinationAccountId;
    private double amount;
    private CommandGateway commandGateway;

    public TransactionSaga(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        sourceAccountId = event.getId();
        destinationAccountId = event.getDestinationAccountId();
        amount = event.getAmount();

        logger.info("A new transfer transaction is started with id {}, from account {} to account {} and amount {}",
                    transactionId, sourceAccountId, destinationAccountId, amount);
        StartTransferWithdrawCommand
            command = new StartTransferWithdrawCommand(sourceAccountId, amount, transactionId);
        commandGateway.send(command);
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferWithdrawConcludedEvent event) {
        logger.info("Transfer transaction with id {}, did transfer from {} successfully",
                    transactionId, sourceAccountId);
        ConcludeTransferDepositCommand command
                = new ConcludeTransferDepositCommand(destinationAccountId, amount, transactionId);
        CancelTransferCommand cancelCommand = new CancelTransferCommand(sourceAccountId, amount, transactionId);
        commandGateway.send(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferFailedToStartEvent event) {
        logger.info("Transfer transaction {} failed to start", transactionId);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferCanceledEvent event) {
        logger.info("Transaction {} was canceled.", transactionId);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferDepositConcludedEvent event) {
        logger.info("Transaction {} concluded", transactionId);
        commandGateway.send(new ReleaseAccountCommand(sourceAccountId, transactionId));
        commandGateway.send(new ReleaseAccountCommand(destinationAccountId, transactionId));
    }
}
