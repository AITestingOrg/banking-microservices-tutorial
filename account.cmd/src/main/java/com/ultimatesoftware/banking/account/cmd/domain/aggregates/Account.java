package com.ultimatesoftware.banking.account.cmd.domain.aggregates;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountInactiveException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.account.common.events.AccountCreatedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountCreditedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountDebitedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountDeletedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountOverdraftedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountUpdatedEvent;
import com.ultimatesoftware.banking.account.common.events.TransferCanceledEvent;
import com.ultimatesoftware.banking.account.common.events.TransferConcludedEvent;
import com.ultimatesoftware.banking.account.common.events.TransferFailedToConcludeEvent;
import com.ultimatesoftware.banking.account.common.events.TransferFailedToStartEvent;
import com.ultimatesoftware.banking.account.common.events.TransferStartedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class Account {
    Logger logger = LoggerFactory.getLogger(Account.class);
    @AggregateIdentifier
    private UUID id;
    private UUID customerId;
    private double balance;
    private boolean active;
    private final double overdraftFee = 35.0;

    @CommandHandler
    public Account(CreateAccountCommand createAccountCommand) {
        apply(new AccountCreatedEvent(createAccountCommand.getId(), createAccountCommand.getCustomerId(), createAccountCommand.getBalance(), createAccountCommand.getActive()));
    }

    public Account(UUID id, UUID customerId, double balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
    }

    public Account(UUID customerId) {
        this.customerId = customerId;
    }

    public Account() {

    }

    public UUID getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    @CommandHandler
    public void on(DebitAccountCommand debitAccountCommand) throws AccountNotEligibleForDebitException, AccountInactiveException {
        if (!active) {
            throw new AccountInactiveException(id);
        }
        if (!AccountRules.eligibleForDebit(this, debitAccountCommand.getAmount())) {
            throw new AccountNotEligibleForDebitException(id, balance);
        }
        double newBalance = balance - debitAccountCommand.getAmount();
        apply(new AccountDebitedEvent(debitAccountCommand.getId(), newBalance, debitAccountCommand.getAmount(), customerId, true, debitAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(CreditAccountCommand creditAccountCommand) throws AccountInactiveException {
        if (!active) {
            throw new AccountInactiveException(id);
        }
        double newBalance = balance + creditAccountCommand.getAmount();
        apply(new AccountCreditedEvent(creditAccountCommand.getId(), customerId, creditAccountCommand.getAmount(), newBalance, true, creditAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(DeleteAccountCommand deleteAccountCommand) throws AccountNotEligibleForDeleteException, AccountInactiveException {
        if (!active) {
            throw new AccountInactiveException(id);
        }
        if (AccountRules.eligibleForDelete(this)) {
            apply(new AccountDeletedEvent(deleteAccountCommand.getId(), deleteAccountCommand.isActive()));
        }
        throw new AccountNotEligibleForDeleteException(id, balance, active);
    }

    @CommandHandler
    public void on(OverDraftAccountCommand overDraftAccountCommand) throws AccountInactiveException {
        if (!active) {
            throw new AccountInactiveException(id);
        }

        if (AccountRules.eligibleForDebitOverdraft(balance, overDraftAccountCommand.getDebitAmount())) {
            double newBalance = balance - overdraftFee;
            apply(new AccountOverdraftedEvent(id, newBalance, customerId, overdraftFee, true, overDraftAccountCommand.getTransactionId()));
        }
    }

    @CommandHandler
    public void on(UpdateAccountCommand updateAccountCommand) {
        apply(new AccountUpdatedEvent(id, updateAccountCommand.getCustomerId()));
    }

    @CommandHandler
    public void on(StartTransferCommand startTransferCommand) throws AccountInactiveException, AccountNotEligibleForDebitException {
        if (!active) {
            apply(new TransferFailedToStartEvent(startTransferCommand.getTransactionId()));
            throw new AccountInactiveException(id);
        }
        if (!AccountRules.eligibleForDebit(this, startTransferCommand.getAmount())) {
            apply(new TransferFailedToStartEvent(startTransferCommand.getTransactionId()));
            throw new AccountNotEligibleForDebitException(id, balance);
        }

        logger.info("Transfer started from {} successfully", id);
        double newBalance = balance - startTransferCommand.getAmount();
        apply(new TransferStartedEvent(startTransferCommand.getTransactionId(),
                                       startTransferCommand.getId(),
                                       newBalance));
    }

    @CommandHandler
    public void on(ConcludeTransferCommand concludeTransferCommand) throws AccountInactiveException {
        if (!active) {
            logger.info("Transfer concluded to {} failed, account not active", id);
            apply(new TransferFailedToConcludeEvent(concludeTransferCommand.getTransactionId()));
            throw new AccountInactiveException(id);
        }
        logger.info("Transfer concluded to {} successfully", id);
        double newBalance = balance + concludeTransferCommand.getAmount();
        apply(new TransferConcludedEvent(concludeTransferCommand.getTransactionId(),
                                         concludeTransferCommand.getId(),
                                         newBalance));
    }

    @CommandHandler
    public void on(CancelTransferCommand cancelTransferCommand) throws AccountInactiveException {
        if (!active) {
            apply(new TransferFailedToConcludeEvent(cancelTransferCommand.getTransactionId()));
            throw new AccountInactiveException(id);
        }
        double newBalance = balance + cancelTransferCommand.getAmount();
        apply(new TransferCanceledEvent(cancelTransferCommand.getTransactionId(),
                                        cancelTransferCommand.getId(),
                                        newBalance));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        id = accountCreatedEvent.getId();
        balance = accountCreatedEvent.getBalance();
        active = accountCreatedEvent.isActive();
        customerId = accountCreatedEvent.getCustomerId();
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent accountDebitedEvent) {
        balance = accountDebitedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent accountCreditedEvent) {
        balance = accountCreditedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(AccountOverdraftedEvent accountOverdraftedEvent) {
        balance = accountOverdraftedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        customerId = accountUpdatedEvent.getCustomerId();
    }

    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        markDeleted();
    }

    @EventSourcingHandler
    public void on(TransferStartedEvent transferStartedEvent) {
        balance = transferStartedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(TransferConcludedEvent transferConcludedEvent) {
        balance = transferConcludedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(TransferCanceledEvent transferCanceledEvent) {
        balance = transferCanceledEvent.getBalance();
    }
}
