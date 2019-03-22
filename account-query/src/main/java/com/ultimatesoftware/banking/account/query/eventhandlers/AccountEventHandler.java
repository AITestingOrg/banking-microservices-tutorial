package com.ultimatesoftware.banking.account.query.eventhandlers;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.operations.AxonEventHandler;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.account.events.*;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.eventhandling.EventHandler;
import org.bson.types.ObjectId;

import javax.inject.Singleton;

@Singleton
public class AccountEventHandler extends AxonEventHandler {
    private final Repository<Account> mongoRepository;

    public AccountEventHandler(Repository<Account> mongoRepository, AxonServerConfiguration axonServerConfiguration) {
        super(axonServerConfiguration);
        this.mongoRepository = mongoRepository;
        LOG.info("Event handler on service started");
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        LOG.info("Account Credited {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(TransferDepositConcludedEvent event) {
        LOG.info("Transfer deposit concluded to {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }
    @EventHandler
    public void on(AccountDebitedEvent event) {
        LOG.info("Account Debited {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(TransferWithdrawConcludedEvent event) {
        LOG.info("Transfer withdraw concluded from  {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(TransferCanceledEvent event) {
        LOG.info("Transfer cancelled from  {}", event.getId());
        updateBalance(event.getId(), event.getBalance());
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        LOG.info("Account Created {}", event.getId());
        mongoRepository.add(new Account(new ObjectId(event.getId()), event.getCustomerId(), event.getBalance())).blockingGet();
    }

    @EventHandler
    public void on(AccountUpdatedEvent event) {
        LOG.info("Account Updated {}", event.getId());
        updateAccount(event.getId(), event);
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        LOG.info("Account Deleted {}", event.getId());
        mongoRepository.deleteOne(event.getId()).blockingGet();
    }

    private void updateBalance(String id, double balance) {
        Account account = mongoRepository.findOne(id).blockingGet();
        LOG.debug("Initial balance {}", account.getBalance());
        account = new Account(account.getId(), account.getCustomerId(), balance);
        mongoRepository.replaceOne(account.getHexId(), account).blockingGet();
        LOG.debug("Updated balance {}", account.getBalance());
    }

    private void updateAccount(String id, AccountUpdatedEvent accountUpdatedEvent) {
        Account account = mongoRepository.findOne(id).blockingGet();
        account = new Account(account.getId(), accountUpdatedEvent.getCustomerId(), account.getBalance());
        mongoRepository.replaceOne(account.getHexId(), account).blockingGet();
        LOG.debug("Updated account {}", account.getBalance());
    }
}
