package com.ultimatesoftware.banking.account.query.domain.eventhandlers;

import com.ultimatesoftware.banking.account.common.events.*;
import com.ultimatesoftware.banking.account.query.domain.models.Account;
import com.ultimatesoftware.banking.account.query.service.repositories.AccountRepository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandler.class);

    @Autowired
    private AccountRepository accountRepository;

    public AccountEventHandler() {}

    @EventHandler
    public void on(AccountCreditedEvent event) {
        LOG.info("Account Credited {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(TransferDepositConcludedEvent event) {
        LOG.info("Transfer concluded to {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }
    @EventHandler
    public void on(AccountDebitedEvent event) {
        LOG.info("Account Debited {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(TransferWithdrawConcludedEvent event) {
        LOG.info("Transfer started from  {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(TransferCanceledEvent event) {
        LOG.info("Transfer started from  {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        LOG.info("Account Created {}", event.getId());
        accountRepository.save(new Account(event.getId(), event.getCustomerId(), event.getBalance()));
    }

    @EventHandler
    public void on(AccountUpdatedEvent event) {
        LOG.info("Account Updated {}", event.getId());
        updateAccount(event.getId(), event);
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        LOG.info("Account Deleted {}", event.getId());
        Account account = accountRepository.findByAccountId(event.getId()).get();
        accountRepository.delete(account);
    }

    private void updateBalance(UUID id, double balance) {
        Account account = accountRepository.findByAccountId(id).get();
        LOG.debug("Initial balance {}", account.getBalance());
        account.setBalance(balance);
        accountRepository.save(account);
        LOG.debug("Updated balance {}", account.getBalance());
    }

    private void updateAccount(UUID id, AccountUpdatedEvent accountUpdatedEvent) {
        Account account = accountRepository.findByAccountId(id).get();
        account.setCustomerId(accountUpdatedEvent.getCustomerId());
        accountRepository.save(account);
        LOG.debug("Updated account {}", account.getBalance());
    }
}
