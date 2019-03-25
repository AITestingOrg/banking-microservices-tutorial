package com.ultimatesoftware.banking.account.transactions.eventhandlers;

import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.account.events.*;
import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import com.ultimatesoftware.banking.account.transactions.models.TransactionStatus;
import com.ultimatesoftware.banking.api.operations.AxonEventHandler;
import com.ultimatesoftware.banking.api.repository.Repository;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.eventhandling.EventHandler;

import javax.inject.Singleton;

@Singleton
public class AccountEventHandlers extends AxonEventHandler {
    private Repository<Transaction> mongoRepository;

    public AccountEventHandlers(Repository<Transaction> mongoRepository, AxonServerConfiguration axonServerConfiguration) {
        super(axonServerConfiguration);
        this.mongoRepository = mongoRepository;
        LOG.info("Event handler on service started");
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        LOG.info("Account Credited {}", event.getId());
        updateTransaction(event);
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        LOG.info("Account Debited {}", event.getId());
        updateTransaction(event);
    }

    @EventHandler
    public void on(TransferFailedToStartEvent event) {
        LOG.info("Transfer failed {}", event.getTransactionId());
        updateTransaction(event.getTransactionId(), TransactionStatus.FAILED);
    }

    @EventHandler
    public void on(TransferCanceledEvent event) {
        LOG.info("Transfer canceled {}", event.getTransactionId());
        updateTransaction(event.getTransactionId(), TransactionStatus.FAILED);
    }

    @EventHandler
    public void on(TransferDepositConcludedEvent event) {
        LOG.info("Transfer completed {}", event.getTransactionId());
        updateTransaction(event.getTransactionId(), TransactionStatus.SUCCESSFUL);
    }

    private void updateTransaction(AccountTransactionEvent event) {
        mongoRepository.findOne(event.getTransactionId()).subscribe(
            transaction -> {
                if (transaction != null) {
                    transaction.setStatus(TransactionStatus.SUCCESSFUL);
                    UpdateResult ur = mongoRepository.replaceOne(transaction.getHexId(), transaction).blockingGet();
                } else {
                    LOG.warn("Attempted to update transaction that does not exist {}.", event.getTransactionId());
                }
        });
    }

    private void updateTransaction(String transactionId, TransactionStatus status) {
        mongoRepository.findOne(transactionId).subscribe(transaction -> {
            if (transaction != null) {
                transaction.setStatus(status);
                UpdateResult ur = mongoRepository.replaceOne(transactionId, transaction).blockingGet();
            } else {
                LOG.warn("Attempted to update transaction that does not exist {}.", transactionId);
            }
        });
    }
}
