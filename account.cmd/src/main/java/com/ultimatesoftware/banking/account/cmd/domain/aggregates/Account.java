package com.ultimatesoftware.banking.account.cmd.domain.aggregates;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountBalanceException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.account.common.events.*;
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
    private int activeTransfers;

    @CommandHandler
    public Account(CreateAccountCommand createAccountCommand) {
        apply(new AccountCreatedEvent(createAccountCommand.getId(), createAccountCommand.getCustomerId(),
                                      createAccountCommand.getBalance(), createAccountCommand.getActive()));
    }

    public Account(UUID id, UUID customerId, double balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.active = active;
        activeTransfers = 0;
    }

    public Account(UUID customerId) {
        this.customerId = customerId;
        activeTransfers = 0;
    }

    public Account() {
        activeTransfers = 0;
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

    public int getActiveTransfers() {
        return activeTransfers;
    }

    @CommandHandler
    public void on(DebitAccountCommand debitAccountCommand) throws AccountNotEligibleForDebitException {
        if (!AccountRules.eligibleForDebit(this, debitAccountCommand.getAmount())) {
            apply(new TransactionFailedEvent(debitAccountCommand.getTransactionId(), "Account balance not eligable for withdraw."));
            throw new AccountNotEligibleForDebitException(id, balance);
        }
        double newBalance = balance - debitAccountCommand.getAmount();
        apply(new AccountDebitedEvent(debitAccountCommand.getId(), customerId, debitAccountCommand.getAmount(), newBalance,
                debitAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(CreditAccountCommand creditAccountCommand) throws AccountBalanceException {
        double newBalance = balance + creditAccountCommand.getAmount();
        if (newBalance == Double.POSITIVE_INFINITY || newBalance == Double.MAX_VALUE) {
            throw new AccountBalanceException("This error is above my pay-grade, I think this guy has too much money.");
        }
        apply(new AccountCreditedEvent(creditAccountCommand.getId(), customerId,
                                       creditAccountCommand.getAmount(), newBalance,
                                       creditAccountCommand.getTransactionId().toString()));
    }

    @CommandHandler
    public void on(DeleteAccountCommand deleteAccountCommand) throws AccountNotEligibleForDeleteException {
        if (AccountRules.eligibleForDelete(this)) {
            apply(new AccountDeletedEvent(deleteAccountCommand.getId(), deleteAccountCommand.isActive()));
            return;
        }
        throw new AccountNotEligibleForDeleteException(id, balance, active);
    }

    @CommandHandler
    public void on(OverDraftAccountCommand overDraftAccountCommand) {
        if (AccountRules.eligibleForDebitOverdraft(balance, overDraftAccountCommand.getDebitAmount())) {
            double newBalance = balance - overdraftFee;
            apply(new AccountOverdraftedEvent(id, customerId, newBalance, overdraftFee,
                    overDraftAccountCommand.getTransactionId()));
        }
    }

    @CommandHandler
    public void on(UpdateAccountCommand updateAccountCommand) {
        apply(new AccountUpdatedEvent(id, updateAccountCommand.getCustomerId()));
    }

    @CommandHandler
    public void on(StartTransferTransactionCommand command) throws AccountNotEligibleForDebitException {
        if (!AccountRules.eligibleForDebit(this, command.getAmount())) {
            apply(new TransferFailedToStartEvent(command.getTransactionId().toString()));
            throw new AccountNotEligibleForDebitException(id, balance);
        }

        logger.info("Transfer transaction started from {} successfully", id);
        apply(new TransferTransactionStartedEvent(command.getId(), command.getDestinationId(), command.getAmount(),
                command.getTransactionId().toString()));
    }

    @CommandHandler
    public void on(StartTransferDepositCommand startTransferDepositCommand) {
        logger.info("Transfer to {} successfully", id);
        double newBalance = balance - startTransferDepositCommand.getAmount();
        apply(new TransferWithdrawConcludedEvent(startTransferDepositCommand.getId(), newBalance,
                startTransferDepositCommand.getTransactionId().toString()));
    }

    @CommandHandler
    public void on(ConcludeTransferCommand concludeTransferCommand) {
        logger.info("Transfer concluded to {} successfully", id);
        double newBalance = balance + concludeTransferCommand.getAmount();
        apply(new TransferDepositConcludedEvent(concludeTransferCommand.getId(), newBalance,
                concludeTransferCommand.getTransactionId().toString()));
    }

    @CommandHandler
    public void on(AcquireSourceAccountCommand command) {
        logger.info("Acquired source account with id {} for transfer {}", id, command.getTransactionId());
        apply(new SourceAccountAcquiredEvent(command.getId(), command.getTransactionId()));
    }

    @CommandHandler
    public void on(AcquireDestinationAccountCommand command) {
        logger.info("Acquired destination account with id {} for transfer {}", id, command.getTransactionId());
        apply(new DestinationAccountAcquiredEvent(command.getId(), command.getTransactionId()));
    }

    @CommandHandler
    public void on(ReleaseAccountCommand releaseAccountCommand) {
        logger.info("Account Released {}", id);
        apply(new AccountReleasedEvent(releaseAccountCommand.getId(), releaseAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(CancelTransferCommand cancelTransferCommand) {
        logger.info("Account transfer canceled from {}", id);
        apply(new TransferCanceledEvent(cancelTransferCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(FailToStartTransferTransactionCommand command) {
        logger.info("Transaction {} failed to start", command.getTransactionId());
        apply(new TransferFailedToStartEvent(command.getTransactionId().toString()));
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
    public void on(TransferWithdrawConcludedEvent transferWithdrawConcludedEvent) {
        balance = transferWithdrawConcludedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(TransferDepositConcludedEvent transferDepositConcludedEvent) {
        balance = transferDepositConcludedEvent.getBalance();
    }

    @EventSourcingHandler
    public void on(DestinationAccountAcquiredEvent destinationAccountAcquiredEvent) {
        activeTransfers++;
    }

    @EventSourcingHandler
    public void on(SourceAccountAcquiredEvent sourceAccountAcquiredEvent) {
        activeTransfers++;
    }

    @EventSourcingHandler
    public void on(AccountReleasedEvent accountReleasedEvent) {
        activeTransfers--;
    }
}
