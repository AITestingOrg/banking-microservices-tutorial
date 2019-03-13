package com.ultimatesoftware.banking.account.cmd.domain.aggregates;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.account.cmd.domain.rules.StandardAccountRules;
import com.ultimatesoftware.banking.events.*;
import com.ultimatesoftware.banking.events.factories.AccountEventType;
import com.ultimatesoftware.banking.events.factories.EventFactory;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Aggregate
public class Account {
    Logger logger = LoggerFactory.getLogger(Account.class);

    @AggregateIdentifier
    private UUID id;
    private String customerId;
    private BigDecimal balance;
    private int activeTransfers;

    private AccountRules accountRules = new StandardAccountRules();

    @CommandHandler
    public Account(CreateAccountCommand createAccountCommand) throws Exception {
        applyEvent(EventFactory.createEvent(
                AccountEventType.CREATED,
                createAccountCommand.getId(),
                createAccountCommand.getCustomerId(),
                createAccountCommand.getBalance()));
    }

    public Account(UUID id, String customerId, BigDecimal balance) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        activeTransfers = 0;
    }

    public Account(String customerId) {
        this.customerId = customerId;
        activeTransfers = 0;
    }

    public Account() {
        activeTransfers = 0;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getActiveTransfers() {
        return activeTransfers;
    }

    public void setAccountRules(AccountRules accountRules) {
        this.accountRules = accountRules;
    }

    @CommandHandler
    public void on(DebitAccountCommand debitAccountCommand) throws Exception {
        if (!this.accountRules.eligibleForDebit(this, debitAccountCommand.getAmount())) {
            applyEvent(EventFactory.createEvent(AccountEventType.TRANSACTION_FAILED, debitAccountCommand.getId(), debitAccountCommand.getTransactionId(), "Account balance not eligable for withdraw."));
            throw new AccountNotEligibleForDebitException(id, balance.doubleValue());
        }
        BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(debitAccountCommand.getAmount()));
        applyEvent(EventFactory.createEvent(AccountEventType.DEBITED, debitAccountCommand.getId(), customerId, debitAccountCommand.getAmount(), newBalance.doubleValue(),
                debitAccountCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(CreditAccountCommand creditAccountCommand) throws Exception {
        if (!this.accountRules.eligibleForCredit(this, creditAccountCommand.getAmount())) {
            applyEvent(EventFactory.createEvent(AccountEventType.TRANSACTION_FAILED, creditAccountCommand.getId(), creditAccountCommand.getTransactionId(), "Account balance not eligable for deposit."));
            throw new AccountNotEligibleForCreditException(id, balance.doubleValue());
        }

        BigDecimal newBalance = balance.add(BigDecimal.valueOf(creditAccountCommand.getAmount()));
        applyEvent(EventFactory.createEvent(AccountEventType.CREDITED, creditAccountCommand.getId(), customerId,
                                       creditAccountCommand.getAmount(), newBalance.doubleValue(),
                                       creditAccountCommand.getTransactionId().toString()));
    }

    @CommandHandler
    public void on(DeleteAccountCommand deleteAccountCommand) throws Exception {
        if (this.accountRules.eligibleForDelete(this)) {
            applyEvent(EventFactory.createEvent(AccountEventType.DELETED, deleteAccountCommand.getId()));
            return;
        }
        throw new AccountNotEligibleForDeleteException(deleteAccountCommand.getId(), balance.doubleValue());
    }

    @CommandHandler
    public void on(UpdateAccountCommand updateAccountCommand) throws Exception {
        applyEvent(EventFactory.createEvent(AccountEventType.UPDATED, id, updateAccountCommand.getCustomerId()));
    }

    @CommandHandler
    public void on(StartTransferTransactionCommand command) throws Exception {
        logger.info("Transfer transaction started from {} successfully", id);
        applyEvent(EventFactory.createEvent(AccountEventType.TRANSACTION_STARTED, id, command.getDestinationId(), command.getAmount(),
                command.getTransactionId()));

    }

    @CommandHandler
    public void on(StartTransferWithdrawCommand command) throws Exception {
        if (!this.accountRules.eligibleForDebit(this, command.getAmount())) {
            applyEvent(EventFactory.createEvent(AccountEventType.TRANSFER_FAILED_TO_START, id, "", command.getTransactionId(), ""));
            throw new AccountNotEligibleForDebitException(id, balance.doubleValue());
        }

        logger.info("Transfer concluded from {} successfully", id);
        BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(command.getAmount()));
        applyEvent(EventFactory.createEvent(AccountEventType.TRANSFER_WITHDRAW_CONCLUDED, id, newBalance.doubleValue(),
                command.getTransactionId()));
    }

    @CommandHandler
    public void on(ConcludeTransferDepositCommand concludeTransferDepositCommand) throws Exception {
        logger.info("Transfer concluded to {} successfully", id);
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(concludeTransferDepositCommand.getAmount()));
        applyEvent(EventFactory.createEvent(AccountEventType.TRANSFER_CONCLUDED, id, newBalance.doubleValue(),
                concludeTransferDepositCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(ReleaseAccountCommand releaseAccountCommand) throws Exception {
        logger.info("Account Released {}", id);
        applyEvent(EventFactory.createEvent(AccountEventType.RELEASED, releaseAccountCommand.getId(), "", releaseAccountCommand.getTransactionId(), ""));
    }

    @CommandHandler
    public void on(CancelTransferCommand cancelTransferCommand) throws Exception {
        logger.info("Account transfer canceled from {}", id);
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(cancelTransferCommand.getAmount()));
        applyEvent(EventFactory.createEvent(AccountEventType.TRANSFER_CANCELLED, cancelTransferCommand.getId(),
                newBalance.doubleValue(), cancelTransferCommand.getTransactionId()));
    }

    @CommandHandler
    public void on(FailToStartTransferTransactionCommand command) throws Exception {
        logger.info("Transaction {} failed to start", command.getTransactionId());
        applyEvent(EventFactory.createEvent(AccountEventType.TRANSFER_FAILED_TO_START, command.getId(), "", command.getTransactionId(), ""));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        id = accountCreatedEvent.getId();
        balance = BigDecimal.valueOf(accountCreatedEvent.getBalance());
        customerId = accountCreatedEvent.getCustomerId();
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent accountDebitedEvent) {
        balance = BigDecimal.valueOf(accountDebitedEvent.getBalance());
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent accountCreditedEvent) {
        balance = BigDecimal.valueOf(accountCreditedEvent.getBalance());
    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        customerId = accountUpdatedEvent.getCustomerId();
    }

    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        delete();
    }

    @EventSourcingHandler
    public void on(TransferWithdrawConcludedEvent transferWithdrawConcludedEvent) {
        activeTransfers++;
        balance = BigDecimal.valueOf(transferWithdrawConcludedEvent.getBalance());
    }

    @EventSourcingHandler
    public void on(TransferDepositConcludedEvent transferDepositConcludedEvent) {
        activeTransfers++;
        balance = BigDecimal.valueOf(transferDepositConcludedEvent.getBalance());
    }
    @EventSourcingHandler
    public void on(TransferCanceledEvent transferCanceledEvent) {
        activeTransfers--;
        balance = BigDecimal.valueOf(transferCanceledEvent.getBalance());
    }

    @EventSourcingHandler
    public void on(AccountReleasedEvent accountReleasedEvent) {
        activeTransfers--;
    }

    public void applyEvent(AccountEvent event) {
        apply(event);
    }

    public void delete() {
        markDeleted();
    }
}
