package com.ultimatesoftware.banking.account.cmd.domain.aggregates;

import com.ultimatesoftware.banking.account.cmd.domain.commands.StartTransferTransactionCommand;
import com.ultimatesoftware.banking.account.cmd.domain.sagas.TransactionSaga;
import com.ultimatesoftware.banking.account.common.events.TransferTransactionStartedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class Transaction {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    @AggregateIdentifier
    private UUID transactionId;
    private UUID fromAccount;
    private UUID toAccount;
    private double amount;
    private long status;

    public Transaction() {
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getFromAccount() {
        return fromAccount;
    }

    public UUID getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public long getStatus() {
        return status;
    }

    @CommandHandler
    public Transaction(StartTransferTransactionCommand command) {
        apply(new TransferTransactionStartedEvent(command.getTransactionId(),
                command.getFromAccount(), command.getToAccount(), command.getAmount()));
    }

    @EventSourcingHandler
    public void on(TransferTransactionStartedEvent command) {
        logger.info("New Transaction");
        transactionId = command.getTransactionId();
        fromAccount = command.getFromAccountId();
        toAccount = command.getToAccountId();
        amount = command.getAmount();
    }
}
