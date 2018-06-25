package com.ultimatesoftware.banking.account.cmd.domain.aggregates;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountBalanceException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.account.common.AccountEventType;
import com.ultimatesoftware.banking.account.common.EventFactory;
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
    private final double overdraftFee = 35.0;
    private int activeTransfers;

    @CommandHandler
    public Account(CreateAccountCommand createAccountCommand) throws Exception {
        apply(EventFactory.createEvent(AccountEventType.CREATED, createAccountCommand.getId(), createAccountCommand.getCustomerId(),
                                      createAccountCommand.getBalance()));
    }

    public Account(UUID id, UUID customerId, double balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
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
    public void on(DebitAccountCommand debitAccountCommand) throws Exception {
        if (!AccountRules.eligibleForDebit(this, debitAccountCommand.getAmount())) {
            apply(EventFactory.createEvent(AccountEventType.TRANSACTION_FAILED, debitAccountCommand.getId(), debitAccountCommand.getTransactionId(), "Account balance not eligable for withdraw."));
            throw new AccountNotEligibleForDebitException(id, balance);
        }
        double newBalance = balance - debitAccountCommand.getAmount();
        apply(EventFactory.createEvent(AccountEventType.DEBITED, debitAccountCommand.getId(), customerId, debitAccountCommand.getAmount(), newBalance,
                debitAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(CreditAccountCommand creditAccountCommand) throws Exception {
        double newBalance = balance + creditAccountCommand.getAmount();
        if (newBalance == Double.POSITIVE_INFINITY || newBalance == Double.MAX_VALUE) {
            throw new AccountBalanceException("This error is above my pay-grade, I think this guy has too much money.");
        }
        apply(EventFactory.createEvent(AccountEventType.CREDITED, creditAccountCommand.getId(), customerId,
                                       creditAccountCommand.getAmount(), newBalance,
                                       creditAccountCommand.getTransactionId().toString()));
    }

    @CommandHandler
    public void on(DeleteAccountCommand deleteAccountCommand) throws Exception {
        if (AccountRules.eligibleForDelete(this)) {
            apply(EventFactory.createEvent(AccountEventType.DELETED, deleteAccountCommand.getId()));
            return;
        }
        throw new AccountNotEligibleForDeleteException(deleteAccountCommand.getId(), balance);
    }

    @CommandHandler
    public void on(UpdateAccountCommand updateAccountCommand) throws Exception {
        apply(EventFactory.createEvent(AccountEventType.UPDATED, id, updateAccountCommand.getCustomerId()));
    }

    @CommandHandler
    public void on(StartTransferTransactionCommand command) throws Exception {
        if (!AccountRules.eligibleForDebit(this, command.getAmount())) {
            apply(EventFactory.createEvent(AccountEventType.TRANSFER_FAILED_TO_START, id, command.getTransactionId().toString()));
            throw new AccountNotEligibleForDebitException(id, balance);
        }

        logger.info("Transfer transaction started from {} successfully", id);
        apply(EventFactory.createEvent(AccountEventType.TRANSFER_STARTED, id, command.getDestinationId(), command.getAmount(),
                command.getTransactionId()));
    }

    @CommandHandler
    public void on(StartTransferDepositCommand startTransferDepositCommand) throws Exception {
        logger.info("Transfer to {} successfully", id);
        double newBalance = balance - startTransferDepositCommand.getAmount();
        apply(EventFactory.createEvent(AccountEventType.TRANSFER_WITHDRAW_CONCLUDED, id, newBalance,
                startTransferDepositCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(ConcludeTransferCommand concludeTransferCommand) throws Exception {
        logger.info("Transfer concluded to {} successfully", id);
        double newBalance = balance + concludeTransferCommand.getAmount();
        apply(EventFactory.createEvent(AccountEventType.TRANSFER_CONCLUDED, id, newBalance,
                concludeTransferCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(AcquireSourceAccountCommand command) throws Exception {
        logger.info("Acquired source account with id {} for transfer {}", id, command.getTransactionId());
        apply(EventFactory.createEvent(AccountEventType.SOURCE_AQUIRED, command.getId(), command.getTransactionId()));
    }

    @CommandHandler
    public void on(AcquireDestinationAccountCommand command) throws Exception {
        logger.info("Acquired destination account with id {} for transfer {}", id, command.getTransactionId());
        apply(EventFactory.createEvent(AccountEventType.DESTINATION_AQUIRED, command.getId(), command.getTransactionId()));
    }

    @CommandHandler
    public void on(ReleaseAccountCommand releaseAccountCommand) throws Exception {
        logger.info("Account Released {}", id);
        apply(EventFactory.createEvent(AccountEventType.RELEASED, releaseAccountCommand.getId(), releaseAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(CancelTransferCommand cancelTransferCommand) throws Exception {
        logger.info("Account transfer canceled from {}", id);
        apply(EventFactory.createEvent(AccountEventType.TRANSFER_CANCELLED, cancelTransferCommand.getId(), cancelTransferCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(FailToStartTransferTransactionCommand command) throws Exception {
        logger.info("Transaction {} failed to start", command.getTransactionId());
        apply(EventFactory.createEvent(AccountEventType.TRANSFER_FAILED_TO_START, command.getId(), command.getTransactionId()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        id = accountCreatedEvent.getId();
        balance = accountCreatedEvent.getBalance();
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
