package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.CreateAccountCommand;
import com.ultimatesoftware.banking.account.cmd.domain.commands.DebitAccountCommand;
import com.ultimatesoftware.banking.account.cmd.domain.commands.DeleteAccountCommand;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.common.events.AccountCreatedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountDebitedEvent;
import com.ultimatesoftware.banking.account.common.events.AccountDeletedEvent;
import com.ultimatesoftware.banking.account.common.events.TransactionFailedEvent;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AggregateLifecycle.class)
public class AccountTest {
    private Account account;

    @Before
    public void setup() {
        mockStatic(AggregateLifecycle.class);
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
    }

    @Test
    public void givenAccountIsEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));

        // act
        account.on(new DeleteAccountCommand(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(AccountDeletedEvent.class));
    }

    @Test(expected = AccountNotEligibleForDeleteException.class)
    public void givenAccountIsNotEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(50.0));

        // act
        account.on(new DeleteAccountCommand(uuid));
    }

    @Test
    public void givenAccountDoesntExist_WhenCreating_CreatedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account();

        // act
        new Account(new CreateAccountCommand(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(AccountCreatedEvent.class));
    }

    @Test
    public void givenAccountWith50Balance_WhenDebiting50_AccountDebittedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(50.0));

        // act
        account.on(new DebitAccountCommand(uuid, 50, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(AccountDebitedEvent.class));
    }

    @Test
    public void givenAccountWith49Balance_WhenDebiting50_TransactionFailedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(49.0));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new DebitAccountCommand(uuid, 50, "test"));
        } catch(AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(TransactionFailedEvent.class));
        Assert.assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWith51Balance_WhenDebiting50_AccountDebittedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(51.0));

        // act
        account.on(new DebitAccountCommand(uuid, 50, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(AccountDebitedEvent.class));
    }

    @Test
    public void givenAccountWith0Balance_WhenDebiting1_TransactionFailedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new DebitAccountCommand(uuid, 1, "test"));
        } catch(AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(TransactionFailedEvent.class));
        Assert.assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWithMaxMinus1Balance_WhenDebitingMax_TransactionFailedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE).subtract(BigDecimal.valueOf(1)));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new DebitAccountCommand(uuid, Double.MAX_VALUE, "test"));
        } catch(AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(TransactionFailedEvent.class));
        Assert.assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWithMaxBalance_WhenDebitingMax_AccountDebittedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE));

        // act
        account.on(new DebitAccountCommand(uuid, Double.MAX_VALUE, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(AccountDebitedEvent.class));
    }

    @Test
    public void givenAccountWithMaxBalance_WhenDebitingMaxMinus1_AccountDebittedEventEmitted() throws Exception {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE));

        // act
        account.on(new DebitAccountCommand(uuid, Double.MAX_VALUE - 1, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(any(AccountDebitedEvent.class));
    }
}
