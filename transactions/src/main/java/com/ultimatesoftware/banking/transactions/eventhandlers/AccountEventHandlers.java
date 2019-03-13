package com.ultimatesoftware.banking.transactions.eventhandlers;

import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.events.*;
import com.ultimatesoftware.banking.transactions.models.Transaction;
import com.ultimatesoftware.banking.transactions.models.TransactionStatus;

import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import javax.inject.Singleton;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AccountEventHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandlers.class);
    private Configuration configurer;
    private final MongoRepository<Transaction> bankTransactionRepository;

    public AccountEventHandlers(MongoRepository<Transaction> bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    @EventListener
    public void configuration(final ServiceStartedEvent event) {
        LOG.info("Configuring Axon server");
        configurer = DefaultConfigurer.defaultConfiguration()
            .eventProcessing(eventProcessingConfigurer -> eventProcessingConfigurer
                .registerEventHandler(conf -> this)).start();
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
        bankTransactionRepository.findOne(event.getTransactionId()).subscribe(
            transaction -> {
                if (transaction != null) {
                    transaction.setStatus(TransactionStatus.SUCCESSFUL);
                    UpdateResult ur = bankTransactionRepository.replaceOne(transaction.getHexId(), transaction).blockingGet();
                } else {
                    LOG.warn("Attempted to update transaction that does not exist {}.", event.getTransactionId());
                }
        });
    }

    private void updateTransaction(String transactionId, TransactionStatus status) {
        bankTransactionRepository.findOne(transactionId).subscribe(transaction -> {
            if (transaction != null) {
                transaction.setStatus(status);
                UpdateResult ur = bankTransactionRepository.replaceOne(transactionId, transaction).blockingGet();
            } else {
                LOG.warn("Attempted to update transaction that does not exist {}.", transactionId);
            }
        });
    }
}
