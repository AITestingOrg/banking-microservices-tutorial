package com.ultimatesoftware.banking.account.cmd.domain.sagas;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.FutureTimeoutException;
import com.ultimatesoftware.banking.account.common.events.*;

import com.ultimatesoftware.banking.eventsourcing.sagas.CustomSaga;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Saga
public class TransactionSaga extends CustomSaga {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSaga.class);

    private UUID transactionId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private double amount;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        fromAccountId = event.getFromAccountId();
        toAccountId = event.getToAccountId();
        amount = event.getAmount();

        logger.info(
                "A new transfer transaction is started with id {}, from account {} to account {} and amount {}",
                transactionId, fromAccountId, toAccountId, amount);
        AcquireSourceAccountCommand command = new AcquireSourceAccountCommand(fromAccountId, transactionId);
        CompletableFuture.supplyAsync(() -> commandGateway.send(command), executor)
                .acceptEither(timeoutAfter(3000),
                        fail -> commandGateway.send(new CancelTransferCommand(command.getTransactionId())));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SourceAccountAcquiredEvent event) {
        logger.info("Account {} acquired successfully for transaction {}", fromAccountId, transactionId);
        AcquireDestinationAccountCommand command = new AcquireDestinationAccountCommand(toAccountId, transactionId);

        CompletableFuture.supplyAsync(() -> commandGateway.send(command), executor)
                .acceptEither(timeoutAfter(3000),
                        fail -> commandGateway.send(new CancelTransferCommand(command.getTransactionId())));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(DestinationAccountAcquiredEvent event) {
        logger.info("Account {} acquired successfully for transaction {}", toAccountId, transactionId);
        commandGateway.send(new StartTransferCommand(transactionId, fromAccountId, amount));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferStartedEvent event) {
        logger.info("Transfer transaction with id {}, did transfer from {} successfully",
                transactionId, fromAccountId);
        commandGateway.send(new ConcludeTransferCommand(transactionId, toAccountId, amount));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferFailedToStartEvent event) {
        logger.info("Account {} failed to transfer for transaction {}", fromAccountId, transactionId);
        commandGateway.send(new ReleaseAccountCommand(fromAccountId, transactionId));
        commandGateway.send(new ReleaseAccountCommand(toAccountId, transactionId));
        commandGateway.send(new CancelTransferCommand(transactionId));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferCanceledEvent event) {
        logger.info("Transaction {} was canceled.", transactionId);
        commandGateway.send(new ReleaseAccountCommand(fromAccountId, transactionId));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferConcludedEvent event) {
        logger.info("Transaction {} concluded", transactionId);
        commandGateway.send(new ReleaseAccountCommand(fromAccountId, transactionId));
        commandGateway.send(new ReleaseAccountCommand(toAccountId, transactionId));
    }

    private CompletableFuture timeoutAfter(long timeout) {
        CompletableFuture result = new CompletableFuture();
        executor.schedule(new FutureTimeoutException(result),
                new Date(System.currentTimeMillis() + timeout));
        return result;
    }
}
