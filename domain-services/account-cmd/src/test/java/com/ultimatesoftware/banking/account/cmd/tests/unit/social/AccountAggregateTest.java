package com.ultimatesoftware.banking.account.cmd.tests.unit.social;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.commands.CreateAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.CreditAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.DebitAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.DeleteAccountCommand;
import com.ultimatesoftware.banking.account.cmd.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.events.*;
import org.axonframework.eventsourcing.AggregateDeletedException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class AccountAggregateTest {
    private FixtureConfiguration<Account> fixture;
    private static final String customerId = "5c86d04877970c1fd879a36b";
    private static final String transactionId = "5c86d04877970c1fd879a36f";

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(Account.class);
    }

    // Happy path tests

    @Test
    public void onAccountCreation() {
        
        double balance = 0.0;
        CreateAccountCommand command = new CreateAccountCommand(customerId);
        fixture.given()
                .when(command)
                .expectEvents(AccountCreatedEvent.builder()
                    .id(command.getId().toHexString())
                    .customerId(customerId)
                    .balance(balance)
                    .build());
    }

    @Test
    public void givenAccountExistsWithBalance_WhenAccountDebit_EmitAccountDebited() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand
            creditCommand = new CreditAccountCommand(createCommand.getId().toHexString(), 100.0, transactionId);
        DebitAccountCommand
            command = new DebitAccountCommand(createCommand.getId().toHexString(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(creditCommand)
                .when(command)
                .expectEvents(AccountDebitedEvent.builder()
                    .id(createCommand.getId().toHexString())
                    .customerId(customerId)
                    .debitAmount(10.0)
                    .balance(90.0)
                    .transactionId(transactionId)
                    .build());
    }

    @Test
    public void onAccountCredit() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId().toHexString(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(AccountCreditedEvent.builder()
                    .id(createCommand.getId().toHexString())
                    .customerId(customerId)
                    .creditAmount(10.0)
                    .balance(10.0)
                    .transactionId(transactionId)
                    .build());
    }

    @Test
    public void onAccountDelete() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId().toHexString());
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(AccountDeletedEvent.builder()
                    .id(createCommand.getId().toHexString())
                    .build());
    }

    // Non-happy path tests

    @Test
    public void onAccountDelete_WhenPositiveBalance_NoEventsSent() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId().toHexString(), 100.0, transactionId);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId().toHexString());
        fixture.givenCommands(createCommand, creditCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void givenInactiveAccount_WhenAccountDebited_ExpectDeletedException() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId().toHexString());
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId().toHexString(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(deleteCommand)
                .when(command)
                .expectException(AggregateDeletedException.class);
    }

    @Test
    public void onAccountCredit_WhenAccountInactive_ExceptionThrown() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId().toHexString());
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId().toHexString(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(deleteCommand)
                .when(command)
                .expectException(AggregateDeletedException.class);
    }

    @Test
    public void onAccountDebit_WhenLargeBalance_DebitSuccessful() {
        double value = BigDecimal.valueOf(Double.MAX_VALUE).divide(BigDecimal.valueOf(2.0)).doubleValue();
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId().toHexString(), value, transactionId);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId().toHexString(), value, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(creditCommand)
                .when(command)
            .expectEvents(AccountDebitedEvent.builder()
                .id(createCommand.getId().toHexString())
                .customerId(customerId)
                .debitAmount(value)
                .balance(0.00)
                .transactionId(transactionId)
                .build());
    }

    @Test
    public void onAccountCredit_WhenLargeBalance_ExceptionThrown() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId().toHexString(), Double.MAX_VALUE / 2, transactionId);
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId().toHexString(), (Double.MAX_VALUE / 2) + 1, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(creditCommand)
                .when(command)
                .expectException(AccountNotEligibleForCreditException.class);
    }

    @Test
    public void onAccountCredit_WhenCreditingMaxBalance_CreditSuccessful() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId().toHexString(), Double.MAX_VALUE, transactionId);
        fixture.givenCommands(createCommand)
                .when(creditCommand)
                .expectEvents(AccountCreditedEvent.builder()
                    .id(createCommand.getId().toHexString())
                    .customerId(customerId)
                    .creditAmount(Double.MAX_VALUE)
                    .balance(Double.MAX_VALUE)
                    .transactionId(transactionId)
                    .build());
    }

    @Test
    public void onAccountDebit_WhenBalanceZero_ExceptionThrown() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId().toHexString(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .when(command)
                .expectException(AccountNotEligibleForDebitException.class);
    }
}
