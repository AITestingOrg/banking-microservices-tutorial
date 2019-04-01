package com.ultimatesoftware.banking.account.cmd.sagas;

import com.ultimatesoftware.banking.account.cmd.commands.*;
import com.ultimatesoftware.banking.account.events.*;
import io.micronaut.context.annotation.Prototype;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class TransactionSaga {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSaga.class);

    private boolean withdrawCompleted = false;
    private boolean depositCompleted = false;
    private String transactionId;
    private double amount;
    private String sourceAccountId;
    private String destinationAccountId;

    @Inject
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferTransactionStartedEvent event) {
        logger.info("A new transfer transaction is started with id {}, from account {} to account {} and amount {}",
            event.getTransactionId(), event.getId(), event.getDestinationAccountId(), event.getAmount());
        try {
            sourceAccountId = event.getId();
            destinationAccountId = event.getDestinationAccountId();
            amount = event.getAmount();
            transactionId = event.getTransactionId();
            DebitAccountCommand command =
                new DebitAccountCommand(event.getId(), event.getAmount(), event.getTransactionId(),
                    true);
            commandGateway.send(command);
        } catch (Exception e) {
            commandGateway.send(new FailTransactionCommand(sourceAccountId, destinationAccountId, transactionId));
        }
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferWithdrawConcludedEvent event) {
        logger.info("Transfer transaction with id {}, did transfer from {} successfully",
            event.getTransactionId(), event.getId());
        withdrawCompleted = true;
        try {
            CreditAccountCommand command
                    = new CreditAccountCommand(destinationAccountId, event.getBalance(), event.getTransactionId(), true);
            commandGateway.send(command);
        } catch (Exception e) {
            commandGateway.send(new FailTransactionCommand(sourceAccountId, destinationAccountId, transactionId, e.getMessage()));
        }
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferFailedEvent event) {
        // change this name and kick of cancel
        logger.info("Transfer transaction {} failed to start", event.getTransactionId());
        commandGateway.send(new CancelTransferCommand(sourceAccountId, transactionId, event.getMsg()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferCanceledEvent event) {
        logger.info("Transaction {} was canceled.", event.getTransactionId());
        if (withdrawCompleted) {
            commandGateway.send(new RevertAccountBalanceCommand(sourceAccountId, amount, transactionId));
        }

        if (depositCompleted) {
            commandGateway.send(new RevertAccountBalanceCommand(destinationAccountId, amount, transactionId));
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(TransferDepositConcludedEvent event) {
        logger.info("Transaction {} concluded", event.getTransactionId());
        depositCompleted = true;
        commandGateway.send(new ReleaseAccountCommand(sourceAccountId, event.getTransactionId()));
        commandGateway.send(new ReleaseAccountCommand(destinationAccountId, event.getTransactionId()));
    }
}
