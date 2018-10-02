package com.ultimatesoftware.banking.account.cmd.integration;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.events.*;
import org.axonframework.eventsourcing.AggregateDeletedException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class AccountAggregateTest {
    private FixtureConfiguration<Account> fixture;
    private static final String customerId = "123e4567-e89b-12d3-a456-426655440000";
    private static final String transactionId = "123e4567-e89b-12d3-a456-426655440010";

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Account.class);
    }

    // Happy path tests

    @Test
    public void onAccountCreation() {
        
        double balance = 0.0;
        boolean active = true;
        CreateAccountCommand command = new CreateAccountCommand(customerId);
        fixture.given()
                .when(command)
                .expectEvents(new AccountCreatedEvent(command.getId(), customerId, balance));
    }

    @Test
    public void onAccountDebit() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0, transactionId);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(creditCommand)
                .when(command)
                .expectEvents(new AccountDebitedEvent(createCommand.getId(), customerId, 10.0, 90.0, transactionId));
    }

    @Test
    public void onAccountCredit() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(new AccountCreditedEvent(createCommand.getId(), customerId, 10.0, 10.00,  transactionId));
    }

    @Test
    public void onAccountDelete() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId());
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(new AccountDeletedEvent(createCommand.getId()));
    }

    // Non-happy path tests

    @Test
    public void onAccountDelete_WhenPositiveBalance_NoEventsSent() {
        
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), 100.0, transactionId);
        DeleteAccountCommand command = new DeleteAccountCommand(createCommand.getId());
        fixture.givenCommands(createCommand, creditCommand)
                .when(command)
                .expectNoEvents();
    }

    @Test
    public void onAccountDebit_WhenAccountInactive_NoEventSent() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId());
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(deleteCommand)
                .when(command)
                .expectException(AggregateDeletedException.class);
    }

    @Test
    public void onAccountCredit_WhenAccountInactive_ExceptionThrown() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DeleteAccountCommand deleteCommand = new DeleteAccountCommand(createCommand.getId());
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(deleteCommand)
                .when(command)
                .expectException(AggregateDeletedException.class);
    }

    @Test
    public void onAccountDebit_WhenLargeBalance_DebitSuccessful() {
        double value = BigDecimal.valueOf(Double.MAX_VALUE).divide(BigDecimal.valueOf(2.0)).doubleValue();
        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), value, transactionId);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), value, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(creditCommand)
                .when(command)
                .expectEvents(new AccountDebitedEvent(createCommand.getId(), customerId, value, 0.00, transactionId));
    }

    @Test
    public void onAccountCredit_WhenLargeBalance_ExceptionThrown() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), Double.MAX_VALUE / 2, transactionId);
        CreditAccountCommand command = new CreditAccountCommand(createCommand.getId(), (Double.MAX_VALUE / 2) + 1, transactionId);
        fixture.givenCommands(createCommand)
                .andGivenCommands(creditCommand)
                .when(command)
                .expectException(AccountNotEligibleForCreditException.class);
    }

    @Test
    public void onAccountCredit_WhenCreditingMaxBalance_CreditSuccessful() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        CreditAccountCommand creditCommand = new CreditAccountCommand(createCommand.getId(), Double.MAX_VALUE, transactionId);
        fixture.givenCommands(createCommand)
                .when(creditCommand)
                .expectEvents(new AccountCreditedEvent(createCommand.getId(), customerId, Double.MAX_VALUE, Double.MAX_VALUE, transactionId));
    }

    @Test
    public void onAccountDebit_WhenBalanceZero_ExceptionThrown() {

        CreateAccountCommand createCommand = new CreateAccountCommand(customerId);
        DebitAccountCommand command = new DebitAccountCommand(createCommand.getId(), 10.0, transactionId);
        fixture.givenCommands(createCommand)
                .when(command)
                .expectException(AccountNotEligibleForDebitException.class);
    }
}
