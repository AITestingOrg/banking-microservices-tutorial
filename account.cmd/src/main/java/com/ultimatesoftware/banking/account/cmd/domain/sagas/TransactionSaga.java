package com.ultimatesoftware.banking.account.cmd.domain.sagas;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.service.scheduling.FutureCommandSend;
import com.ultimatesoftware.banking.events.*;

import com.ultimatesoftware.banking.eventsourcing.sagas.CustomSaga;

import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Saga
public class TransactionSaga extends CustomSaga {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSaga.class);

    private String transactionId;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private double amount;
    private ScheduledFuture cancellationTimer;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        sourceAccountId = event.getId();
        destinationAccountId = event.getDestinationAccountId();
        amount = event.getAmount();

        logger.info("A new transfer transaction is started with id {}, from account {} to account {} and amount {}",
                    transactionId, sourceAccountId, destinationAccountId, amount);
        StartTransferWithdrawCommand command = new StartTransferWithdrawCommand(sourceAccountId, amount, transactionId);
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

        cancellationTimer = executor.schedule(new FutureCommandSend(commandGateway, cancelCommand),
                                              new Date(System.currentTimeMillis() + 10000));
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
        cancellationTimer.cancel(false);
    }
}
