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
    public void on(AccountDebitedEvent event) {
        LOG.info("Account Debited {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(AccountOverdraftedEvent event) {
        LOG.info("Account Overdraft {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        LOG.info("Account Created {}", event.getId());
        accountRepository.save(new Account(event.getId(), event.getCustomerId(), event.getBalance(), event.isActive()));
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        LOG.info("Account Deleted {}", event.getId());
        Account account = accountRepository.findOne(event.getId());
        account.setActive(false);
        accountRepository.save(account);
    }

    private void updateBalance(UUID id, double balance) {
        Account account = accountRepository.findOne(id);
        LOG.debug("Initial balance {}", account.getBalance());
        account.setBalance(balance);
        accountRepository.save(account);
        LOG.debug("Updated balance {}", account.getBalance());
    }
}
