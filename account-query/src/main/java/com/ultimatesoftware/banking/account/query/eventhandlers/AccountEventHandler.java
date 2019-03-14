package com.ultimatesoftware.banking.account.query.eventhandlers;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.events.*;
import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import javax.inject.Singleton;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventhandling.EventHandler;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AccountEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandler.class);
    private Configuration configurer;
    private AxonServerConfiguration axonServerConfiguration;

    private MongoRepository<Account> mongoRepository;

    public AccountEventHandler(MongoRepository<Account> mongoRepository, AxonServerConfiguration axonServerConfiguration) {
        this.mongoRepository = mongoRepository;
        this.axonServerConfiguration = axonServerConfiguration;
        LOG.info("Event handler on service started");
    }

    @EventListener
    public void configuration(final ServiceStartedEvent event) {
        LOG.info("Configuring Axon server");
        configurer = DefaultConfigurer.defaultConfiguration()
            .registerComponent(AxonServerConfiguration.class, c -> axonServerConfiguration)
            .eventProcessing(eventProcessingConfigurer -> eventProcessingConfigurer
                .registerEventHandler(conf -> this)).start();
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
