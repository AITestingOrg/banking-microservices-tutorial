package com.ultimatesoftware.banking.account.cmd.aggregates;

import com.ultimatesoftware.banking.account.cmd.commands.*;
import com.ultimatesoftware.banking.account.cmd.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.rules.AccountRules;
import com.ultimatesoftware.banking.account.cmd.rules.StandardAccountRules;
import com.ultimatesoftware.banking.account.events.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@NoArgsConstructor
public class Account {
    Logger logger = LoggerFactory.getLogger(Account.class);

    @AggregateIdentifier
    private @Getter ObjectId id;
    private @Getter String customerId;
    private @Getter BigDecimal balance;
    private @Getter boolean activeTransfer;

    private AccountRules accountRules = new StandardAccountRules();

    @CommandHandler
    public Account(CreateAccountCommand createAccountCommand) throws Exception {
        applyEvent(AccountCreatedEvent.builder()
            .id(createAccountCommand.getId().toHexString())
            .balance(createAccountCommand.getBalance())
            .customerId(createAccountCommand.getCustomerId())
            .build());
    }

    public Account(ObjectId id, String customerId, BigDecimal balance) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        activeTransfer = false;
    }

    public void setAccountRules(AccountRules accountRules) {
        this.accountRules = accountRules;
    }

    @CommandHandler
    public void on(DebitAccountCommand debitAccountCommand) throws Exception {
        logger.info("Handling Debit Command for " + debitAccountCommand.getTransactionId());
        if (!this.accountRules.eligibleForDebit(this, debitAccountCommand.getAmount())) {
            if (debitAccountCommand.isTransfer()) {
                applyEvent(TransferFailedEvent.builder()
                    .id(debitAccountCommand.getId().toHexString())
                    .msg(String.format("Account %s balance not eligible for withdraw.",
                        debitAccountCommand.getId().toHexString()))
                    .transactionId(debitAccountCommand.getTransactionId())
                    .build());
            } else {
                applyEvent(TransactionFailedEvent.builder()
                    .id(debitAccountCommand.getId().toHexString())
                    .msg(String.format("Account %s balance not eligible for withdraw.",
                        debitAccountCommand.getId().toHexString()))
                    .transactionId(debitAccountCommand.getTransactionId())
                    .build());
            }
            throw new AccountNotEligibleForDebitException(id.toHexString(),
                    balance.doubleValue());
        }
        BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(debitAccountCommand.getAmount()));
        if (debitAccountCommand.isTransfer()) {
            applyEvent(TransferWithdrawConcludedEvent.builder()
                .id(debitAccountCommand.getId().toHexString())
                .balance(newBalance.doubleValue())
                .amount(debitAccountCommand.getAmount())
                .transactionId(debitAccountCommand.getTransactionId())
                .build());
        } else {
            applyEvent(AccountDebitedEvent.builder()
                .id(debitAccountCommand.getId().toHexString())
                .balance(newBalance.doubleValue())
                .debitAmount(debitAccountCommand.getAmount())
                .customerId(customerId)
                .transactionId(debitAccountCommand.getTransactionId())
                .build());
        }
    }

    @CommandHandler
    public void on(CreditAccountCommand creditAccountCommand) throws Exception {
        logger.info("Handling Credit Command for " + creditAccountCommand.getTransactionId());
        if (!this.accountRules.eligibleForCredit(this, creditAccountCommand.getAmount())) {
            if (creditAccountCommand.isTransfer()) {
                applyEvent(TransferFailedEvent.builder()
                    .id(creditAccountCommand.getId().toHexString())
                    .msg(String.format("Account %s not eligible for deposit.",
                        creditAccountCommand.getId().toHexString()))
                    .transactionId(creditAccountCommand.getTransactionId())
                    .build());
            } else {
                applyEvent(TransactionFailedEvent.builder()
                    .id(creditAccountCommand.getId().toHexString())
                    .transactionId(creditAccountCommand.getTransactionId())
                    .msg(String.format("Account %s balance not eligible for deposit.",
                        id.toHexString()))
                    .build());
            }
            throw new AccountNotEligibleForCreditException(id.toHexString(), balance.doubleValue());
        }

        BigDecimal newBalance = balance.add(BigDecimal.valueOf(creditAccountCommand.getAmount()));
        if (creditAccountCommand.isTransfer()) {
            applyEvent(TransferDepositConcludedEvent.builder()
                .id(creditAccountCommand.getId().toHexString())
                .balance(newBalance.doubleValue())
                .transactionId(creditAccountCommand.getTransactionId())
                .build());
        } else {
            applyEvent(AccountCreditedEvent.builder()
                .id(creditAccountCommand.getId().toHexString())
                .customerId(customerId)
                .balance(newBalance.doubleValue())
                .creditAmount(creditAccountCommand.getAmount())
                .transactionId(creditAccountCommand.getTransactionId())
                .build());
        }
    }

    @CommandHandler
    public void on(RevertAccountBalanceCommand revertAccountBalanceCommand) {
        logger.info("Handling Revert Account Balance Command for " + revertAccountBalanceCommand.getTransactionId());
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(revertAccountBalanceCommand.getAmount()));
        applyEvent(BalanceRevertedEvent.builder()
            .id(revertAccountBalanceCommand.getId())
            .balance(newBalance)
            .transactionId(revertAccountBalanceCommand.getTransactionId())
            .build());
    }

    @CommandHandler
    public void on(DeleteAccountCommand deleteAccountCommand) throws Exception {
        logger.info("Handling Delete Command for " + deleteAccountCommand.getId());
        if (this.accountRules.eligibleForDelete(this)) {
            applyEvent(AccountDeletedEvent.builder()
                .id(deleteAccountCommand.getId().toHexString())
                .build());
            return;
        }
        logger.warn("Account with ineligible balance requested for delete. Account ID: " + deleteAccountCommand.getId().toHexString());
        throw new AccountNotEligibleForDeleteException(deleteAccountCommand.getId().toHexString(), this.balance.doubleValue());
    }

    @CommandHandler
    public void on(UpdateAccountCommand updateAccountCommand) throws Exception {
        logger.info("Handling Update Command for " + updateAccountCommand.getId());
        applyEvent(AccountUpdatedEvent.builder()
            .id(updateAccountCommand.getId().toHexString())
            .customerId(updateAccountCommand.getCustomerId())
            .build());
    }

    @CommandHandler
    public void on(StartTransferCommand command) throws Exception {
        logger.info("Transfer transaction started from {} successfully", id);
        applyEvent(TransferTransactionStartedEvent.builder()
            .id(command.getId().toHexString())
            .amount(command.getTransactionDto().getAmount())
            .destinationAccountId(command.getTransactionDto().getDestinationAccountId())
            .transactionId(command.getTransactionId())
            .build());
    }

    @CommandHandler
    public void on(FailTransactionCommand command) throws Exception {
        logger.info("Transfer transaction %s failed.", id);
        applyEvent(TransferFailedEvent.builder()
            .id(command.getId().toHexString())
            .msg(command.getMsg())
            .transactionId(command.getTransactionId())
            .build());
    }

    @CommandHandler
    public void on(ReleaseAccountCommand releaseAccountCommand) throws Exception {
        logger.info("Account Released {}", id);
        applyEvent(AccountReleasedEvent.builder()
            .id(releaseAccountCommand.getId().toHexString())
            .transactionId(releaseAccountCommand.getTransactionId())
            .build());
    }

    @CommandHandler
    public void on(CancelTransferCommand cancelTransferCommand) throws Exception {
        logger.info("Account transfer canceled from {}", id);
        applyEvent(TransferCanceledEvent.builder()
            .id(cancelTransferCommand.getId().toHexString())
            .transactionId(cancelTransferCommand.getTransactionId())
            .build());
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        id = new ObjectId(accountCreatedEvent.getId());
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
    public void on(BalanceRevertedEvent balanceRevertedEvent) {
        balance = BigDecimal.valueOf(balanceRevertedEvent.getBalance());
        activeTransfer = false;
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
        activeTransfer = true;
        balance = BigDecimal.valueOf(transferWithdrawConcludedEvent.getBalance());
    }

    @EventSourcingHandler
    public void on(TransferDepositConcludedEvent transferDepositConcludedEvent) {
        activeTransfer = true;
        balance = BigDecimal.valueOf(transferDepositConcludedEvent.getBalance());
    }

    @EventSourcingHandler
    public void on(AccountReleasedEvent accountReleasedEvent) {
        activeTransfer = false;
    }

    public void applyEvent(AccountEvent event) {
        apply(event);
    }

    public void delete() {
        markDeleted();
    }
}
