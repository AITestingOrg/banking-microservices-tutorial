package com.ultimatesoftware.banking.account.cmd.integration;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionDto;
import com.ultimatesoftware.banking.events.*;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AggregateLifecycle.class})
public class AccountTest {
    private Account account;

    private static final UUID uuid = UUID.randomUUID();

    @Before
    public void setup() {
        mockStatic(AggregateLifecycle.class);
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
    }

    @Test
    public void givenAccountIsEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));

        // act
        account.on(new DeleteAccountCommand(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDeletedEvent>any());
    }

    @Test(expected = AccountNotEligibleForDeleteException.class)
    public void givenAccountIsNotEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(50.0));

        // act
        account.on(new DeleteAccountCommand(uuid));
    }

    @Test
    public void givenAccountDoesntExist_WhenCreating_CreatedEventEmitted() throws Exception {
        // arrange
        account = new Account();

        // act
        new Account(new CreateAccountCommand(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountCreatedEvent>any());
    }

    @Test
    public void givenAccountWith50Balance_WhenDebiting50_AccountDebittedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(50.0));

        // act
        account.on(new DebitAccountCommand(uuid, 50, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDebitedEvent>any());
    }

    @Test
    public void givenAccountWith49Balance_WhenDebiting50_TransactionFailedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(49.0));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new DebitAccountCommand(uuid, 50, "test"));
        } catch (AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransactionFailedEvent>any());
        assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWith51Balance_WhenDebiting50_AccountDebittedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(51.0));

        // act
        account.on(new DebitAccountCommand(uuid, 50, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDebitedEvent>any());
    }

    @Test
    public void givenAccountWith0Balance_WhenDebiting1_TransactionFailedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new DebitAccountCommand(uuid, 1, "test"));
        } catch (AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransactionFailedEvent>any());
        assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWithMaxMinus1Balance_WhenDebitingMax_TransactionFailedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE).subtract(BigDecimal.valueOf(1)));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new DebitAccountCommand(uuid, Double.MAX_VALUE, "test"));
        } catch (AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransactionFailedEvent>any());
        assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWithMaxBalance_WhenDebitingMax_AccountDebitedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE));

        // act
        account.on(new DebitAccountCommand(uuid, Double.MAX_VALUE, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDebitedEvent>any());
    }

    @Test
    public void givenAccountWithMaxBalance_WhenDebitingMaxMinus1_AccountDebitedEventEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE));

        // act
        account.on(new DebitAccountCommand(uuid, Double.MAX_VALUE - 1, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDebitedEvent>any());
    }

    @Test
    public void givenAccountWithMaxBalance_WhenCrediting1_TransactionFailedIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new CreditAccountCommand(uuid, 1.0, "test"));
        } catch (AccountNotEligibleForCreditException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransactionFailedEvent>any());
        assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWithMaxBalanceMinus1_WhenCrediting1_AccountCreditedIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE).subtract(BigDecimal.valueOf(1.0)));

        // act
        account.on(new CreditAccountCommand(uuid, 1.0, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountCreditedEvent>any());
    }

    @Test
    public void givenAccountWith0Balance_WhenCrediting1_AccountCreditedIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));

        // act
        account.on(new CreditAccountCommand(uuid, 1.0, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountCreditedEvent>any());
    }

    @Test
    public void givenAccountWith0Balance_WhenCreditingMax_AccountCreditedIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));

        // act
        account.on(new CreditAccountCommand(uuid, Double.MAX_VALUE, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountCreditedEvent>any());
    }

    @Test
    public void givenAccountWith0Balance_WhenStartingTransaction_TransactionFailedToStartIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));
        boolean exceptionThrown = false;

        // act
        try {
            account.on(new StartTransferWithdrawCommand(uuid, 10.0, uuid.toString()));
        } catch (AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransferFailedToStartEvent>any());
        assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountWith10Balance_WhenStartingTransactionFor10_TransactionFailedToStartIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(10.0));

        // act
        account.on(new StartTransferTransactionCommand(new TransactionDto(uuid, uuid, 10.0, uuid)));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransferTransactionStartedEvent>any());
    }
}
