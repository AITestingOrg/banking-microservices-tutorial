package com.ultimatesoftware.banking.transactions.domain.eventhandlers;

import com.ultimatesoftware.banking.events.*;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionStatus;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import java.util.Optional;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountEventHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandlers.class);

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

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
        Optional<BankTransaction> transactionOp = bankTransactionRepository.findById(event.getTransactionId());
        if (transactionOp.isPresent()) {
            BankTransaction transaction = transactionOp.get();
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            bankTransactionRepository.save(transaction);
            return;
        }
        LOG.warn("Attempted to update transaction that does not exist {}.", event.getTransactionId());
    }

    private void updateTransaction(String transactionId, TransactionStatus status) {
        Optional<BankTransaction> transactionOp = bankTransactionRepository.findById(transactionId);
        if (transactionOp.isPresent()) {
            BankTransaction transaction = transactionOp.get();
            transaction.setStatus(status);
            bankTransactionRepository.save(transaction);
            return;
        }
        LOG.warn("Attempted to update transaction that does not exist {}.", transactionId);
    }
}
