package com.ultimatesoftware.banking.account.cmd.domain.aggregates;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountInactiveException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.account.common.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.validation.constraints.Min;
import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class Account {
    @AggregateIdentifier
    private UUID id;
    private String customerId;
    private double balance;
    private boolean active;
    private final double overdraftFee = 35.0;

    @CommandHandler
    public Account(CreateAccountCommand createAccountCommand) {
        apply(new AccountCreatedEvent(createAccountCommand.getId(), createAccountCommand.getCustomerId(), createAccountCommand.getBalance(), createAccountCommand.getActive()));
    }

    public Account(UUID id, String customerId, double balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
    }

    public Account(String customerId) {
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

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    @CommandHandler
    public void on(DebitAccountCommand debitAccountCommand) throws AccountNotEligibleForDebitException, AccountInactiveException {
        if(!active) {
            throw new AccountInactiveException(id);
        }
        if(!AccountRules.eligibleForDebit(this, debitAccountCommand.getAmount())) {
            throw new AccountNotEligibleForDebitException(id, balance);
        }
        double newBalance = balance - debitAccountCommand.getAmount();
        apply(new AccountDebitedEvent(debitAccountCommand.getId(), newBalance));
    }

    @CommandHandler
    public void on(CreditAccountCommand creditAccountCommand) throws AccountInactiveException {
        if(!active) {
            throw new AccountInactiveException(id);
        }
        double newBalance = balance + creditAccountCommand.getAmount();
        apply(new AccountCreditedEvent(creditAccountCommand.getId(), newBalance));
    }

    @CommandHandler
    public void on(DeleteAccountCommand deleteAccountCommand) throws AccountNotEligibleForDeleteException, AccountInactiveException {
        if(!active) {
            throw new AccountInactiveException(id);
        }
        if(AccountRules.eligibleForDelete(this)) {
            apply(new AccountDeletedEvent(deleteAccountCommand.getId(), deleteAccountCommand.isActive()));
        }
        throw new AccountNotEligibleForDeleteException(id, balance, active);
    }

    @CommandHandler
    public void on(OverDraftAccountCommand overDraftAccountCommand) throws AccountInactiveException {
        if(!active) {
            throw new AccountInactiveException(id);
        }

        if(AccountRules.eligibleForDebitOverdraft(balance, overDraftAccountCommand.getDebitAmount())) {
            double newBalance = balance - overdraftFee;
            apply(new AccountOverdraftedEvent(id, newBalance, overDraftAccountCommand.getDebitAmount()));
        }
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
    public void on(AccountDeletedEvent accountDeletedEvent) {
        markDeleted();
    }
}
