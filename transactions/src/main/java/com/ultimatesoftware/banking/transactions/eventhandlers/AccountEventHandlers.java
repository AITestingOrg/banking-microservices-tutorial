package com.ultimatesoftware.banking.transactions.eventhandlers;

import com.ultimatesoftware.banking.events.*;
import com.ultimatesoftware.banking.transactions.models.TransactionStatus;
import com.ultimatesoftware.banking.transactions.respositories.TransactionRepository;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountEventHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandlers.class);

    private TransactionRepository bankTransactionRepository;

    public AccountEventHandlers() {}

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
                    bankTransactionRepository.replaceOne(transaction.getId(), transaction);
                } else {
                    LOG.warn("Attempted to update transaction that does not exist {}.", event.getTransactionId());
                }
        });
    }

    private void updateTransaction(String transactionId, TransactionStatus status) {
        bankTransactionRepository.findOne(transactionId).subscribe(transaction -> {
            if (transaction != null) {
                transaction.setStatus(status);
                bankTransactionRepository.replaceOne(transactionId, transaction);
            } else {
                LOG.warn("Attempted to update transaction that does not exist {}.", transactionId);
            }
        });
    }
}
