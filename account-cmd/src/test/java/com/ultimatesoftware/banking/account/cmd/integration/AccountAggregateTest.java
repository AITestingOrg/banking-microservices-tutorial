package com.ultimatesoftware.banking.account.cmd.integration;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.common.events.*;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountAggregateTest {
    private FixtureConfiguration<Account> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(Account.class);
    }

    // Happy path tests

    @Test
    public void OnAccountCreation() {
        String customerId = "Doe";
        double balance = 0.0;
        boolean active = true;
        CreateAccountCommand command = new CreateAccountCommand(customerId);
        fixture.given()
                .when(command)
                .expectEvents(new AccountCreatedEvent(command.getId(), customerId, balance, active));
    }

    @Test
    public void OnAccountDebit() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), 10.0);
        fixture.givenCommands(createCommand, creditCommand)
                .when(command)
                .expectEvents(new AccountDebitedEvent(createCommand.getId(), 90.0));
    }

    @Test
    public void OnAccountCredit() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId(), 10.0);
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(new AccountCreditedEvent(createCommand.getId(), 10.0));
    }

    @Test
    public void OnAccountOverdraft() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0);
        DebitAccountCommand debitCommand = new DebitAccountCommand(createCommand.getId(), 101.0);
        OverDraftAccountCommand command = new OverDraftAccountCommand(createCommand.getId(), 101.0);
        fixture.givenCommands(createCommand, creditCommand, debitCommand)
                .when(command)
                .expectEvents(new AccountOverdraftedEvent(createCommand.getId(), -36.0, 101.0));
    }

    @Test
    public void OnAccountDelete() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId());
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(new AccountDeletedEvent(createCommand.getId(), false));
    }

    // Non-happy path tests

    @Test
    public void OnAccountDelete_WhenPositiveBalance_NoEventsSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId());
        fixture.givenCommands(createCommand, creditCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void OnAccountDelete_WhenNegativeBalance_NoEventsSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0);
        DebitAccountCommand debitCommand = new DebitAccountCommand(createCommand.getId(), 101.0);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId());
        fixture.givenCommands(createCommand, creditCommand, debitCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void OnAccountOverdraft_WhenPositiveBalance_NoEventSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0);
        DebitAccountCommand debitCommand = new DebitAccountCommand(createCommand.getId(), 10.0);
        OverDraftAccountCommand command = new OverDraftAccountCommand(createCommand.getId(), 10.0);
        fixture.givenCommands(createCommand, creditCommand, debitCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void OnAccountDebit_WhenNegativeBalance_NoEventSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0);
        DebitAccountCommand debitCommand = new DebitAccountCommand(createCommand.getId(), 101.0);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), 10.0);
        fixture.givenCommands(createCommand, creditCommand, debitCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void OnAccountDebit_WhenAccountInactive_NoEventSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId());
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), 10.0);
        fixture.givenCommands(createCommand, deleteCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void OnAccountCredit_WhenAccountInactive_NoEventSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId());
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId(), 10.0);
        fixture.givenCommands(createCommand, deleteCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void OnAccountDelete_WhenAccountInactive_NoEventSent() {
        String customerId = "Doe";
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId());
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId());
        fixture.givenCommands(createCommand, deleteCommand)
                .when(command)
                .expectNoEvents();
    }
}
