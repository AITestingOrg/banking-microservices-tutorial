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

    private String transactionId;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private double amount;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        sourceAccountId = event.getId();
        destinationAccountId = event.getDestinationAccountId();
        amount = event.getAmount();

        logger.info("A new transfer transaction is started with id {}, from account {} to account {} and amount {}",
                    transactionId, sourceAccountId, destinationAccountId, amount);
        AcquireSourceAccountCommand command = new AcquireSourceAccountCommand(sourceAccountId, transactionId);
        commandGateway.send(command);
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SourceAccountAcquiredEvent event) {
        logger.info("Account {} acquired successfully for transaction {}", sourceAccountId, transactionId);
        AcquireDestinationAccountCommand command = new AcquireDestinationAccountCommand(destinationAccountId, transactionId);

        sendWithTimeout(command, new CancelTransferCommand(sourceAccountId, transactionId));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(DestinationAccountAcquiredEvent event) {
        logger.info("Account {} acquired successfully for transaction {}", destinationAccountId, transactionId);
        commandGateway.send(new StartTransferDepositCommand(amount, sourceAccountId, transactionId));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferWithdrawConcludedEvent event) {
        logger.info("Transfer transaction with id {}, did transfer from {} successfully",
                    transactionId, sourceAccountId);
        commandGateway.send(new ConcludeTransferCommand(amount, destinationAccountId, transactionId));
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
        commandGateway.send(new ReleaseAccountCommand(sourceAccountId, transactionId));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferDepositConcludedEvent event) {
        logger.info("Transaction {} concluded", transactionId);
        commandGateway.send(new ReleaseAccountCommand(sourceAccountId, transactionId));
        commandGateway.send(new ReleaseAccountCommand(destinationAccountId, transactionId));
    }

    private void sendWithTimeout(TransactionCommand command, TransactionCommand timeoutCommand) {
        CompletableFuture.supplyAsync(() -> commandGateway.send(command), executor)
                .acceptEither(timeoutAfter(3000),
                        fail -> commandGateway.send(timeoutCommand));
    }

    private CompletableFuture timeoutAfter(long timeout) {
        CompletableFuture result = new CompletableFuture();
        executor.schedule(new FutureTimeoutException(result),
                new Date(System.currentTimeMillis() + timeout));
        return result;
    }
}
