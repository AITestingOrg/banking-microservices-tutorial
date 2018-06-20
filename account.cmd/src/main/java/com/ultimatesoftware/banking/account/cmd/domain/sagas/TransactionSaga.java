package com.ultimatesoftware.banking.account.cmd.domain.sagas;

import com.ultimatesoftware.banking.account.cmd.domain.commands.CancelTransferCommand;
import com.ultimatesoftware.banking.account.cmd.domain.commands.ConcludeTransferCommand;
import com.ultimatesoftware.banking.account.cmd.domain.commands.StartTransferCommand;
import com.ultimatesoftware.banking.account.common.events.*;
import com.ultimatesoftware.banking.eventsourcing.sagas.CustomSaga;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;
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
                "A new transfer transaction is started with id {}, from account {} to account {} and amount",
                transactionId, fromAccountId, toAccountId, amount);
        commandGateway.send(new StartTransferCommand(transactionId, fromAccountId, amount));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferStartedEvent event) {
        logger.info("Transfer transaction with id {}, did transfer from {} successfully",
                transactionId, fromAccountId);
        CompletableFuture future = commandGateway.send(new ConcludeTransferCommand(transactionId, toAccountId, amount));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferFailedToConcludeEvent event) {
        logger.info("Transaction {} failed to conclude", transactionId);
        commandGateway.send(new CancelTransferCommand(transactionId, fromAccountId, amount));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferCanceledEvent event) {
        logger.info("Transaction {} was canceled.", transactionId);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferConcludedEvent event) {
        logger.info("Transaction {} concluded", transactionId);
    }
}
