package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.account.common.events.*;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AggregateLifecycle.class, AccountRules.class})
public class AccountTest {
    private Account account;

    private static final UUID uuid = UUID.randomUUID();

    @Before
    public void setup() throws Exception {
        mockStatic(AggregateLifecycle.class);
        mockStatic(AccountRules.class);
    }

    @Test
    public void givenAccountIsEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());

        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));
        when(AccountRules.eligibleForDelete(account)).thenReturn(true);

        // act
        account.on(new DeleteAccountCommand(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDeletedEvent>any());
    }

    @Test(expected = AccountNotEligibleForDeleteException.class)
    public void givenAccountIsNotEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
        account = new Account(uuid, uuid, BigDecimal.valueOf(50.0));
        when(AccountRules.eligibleForDelete(account)).thenReturn(false);

        // act
        account.on(new DeleteAccountCommand(uuid));
    }

    @Test
    public void givenAccountEligibleForDebit_WhenDebiting_AccountDebitedEventEmitted() throws Exception {
        // arrange
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
        account = new Account(uuid, uuid, BigDecimal.valueOf(50.0));
        when(AccountRules.eligibleForDebit(account, anyDouble())).thenReturn(true);

        // act
        account.on(new DebitAccountCommand(uuid, 49.0, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountDebitedEvent>any());
    }

    @Test
    public void givenAccountInEligibleForDebit_WhenDebiting_TransactionFailedEventEmitted() throws Exception {
        // arrange
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
        account = new Account(uuid, uuid, BigDecimal.valueOf(49.0));
        boolean exceptionThrown = false;
        when(AccountRules.eligibleForDebit(account, anyDouble())).thenReturn(false);

        // act
        try {
            account.on(new DebitAccountCommand(uuid, 50.0, "test"));
        } catch (AccountNotEligibleForDebitException e) {
            exceptionThrown = true;
        }

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<TransactionFailedEvent>any());
        assertEquals(true, exceptionThrown);
    }

    @Test
    public void givenAccountInEligibleForCredit_WhenCrediting_TransactionFailedIsEmitted() throws Exception {
        // arrange
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE));
        boolean exceptionThrown = false;
        when(AccountRules.eligibleForCredit(account, anyDouble())).thenReturn(false);

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
    public void givenAccountEligibleForCredit_WhenCrediting_AccountCreditedIsEmitted() throws Exception {
        // arrange
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
        account = new Account(uuid, uuid, BigDecimal.valueOf(Double.MAX_VALUE).subtract(BigDecimal.valueOf(1.0)));
        when(AccountRules.eligibleForCredit(account, anyDouble())).thenReturn(true);

        // act
        account.on(new CreditAccountCommand(uuid, 2.0, "test"));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountCreditedEvent>any());
    }

    @Test
    public void givenAccountExists_WhenUpdating_AccountUpdatedIsEmitted() throws Exception {
        // arrange
        account = new Account(uuid, uuid, BigDecimal.valueOf(0.0));

        // act
        account.on(new UpdateAccountCommand(uuid, uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(Matchers.<AccountUpdatedEvent>any());
    }


    @Test
    public void givenAcountCreatedEmitted_whenHandling_ThenUpdateIdBalanceCustomerId() throws Exception {
        // arrange
        UUID customerId = UUID.randomUUID();
        account = new Account();

        // act
        account.on(new AccountCreatedEvent(uuid, customerId, 0.0));

        // assert
        assertEquals(BigDecimal.valueOf(0.0), account.getBalance());
        assertEquals(uuid, account.getId());
        assertEquals(customerId, account.getCustomerId());
    }

    @Test
    public void givenAcountDebitedEmitted_whenHandling_ThenUpdateBalance() throws Exception {
        // arrange
        UUID customerId = UUID.randomUUID();
        account = new Account();

        // act
        account.on(new AccountDebitedEvent(uuid, customerId, 10.0, 10.0, "test"));

        // assert
        assertEquals(BigDecimal.valueOf(10.0), account.getBalance());
    }

    @Test
    public void givenAcountCreditedEmitted_whenHandling_ThenUpdateBalance() throws Exception {
        // arrange
        UUID customerId = UUID.randomUUID();
        account = new Account();

        // act
        account.on(new AccountCreditedEvent(uuid, customerId, 10.0, 10.0, "test"));

        // assert
        assertEquals(BigDecimal.valueOf(10.0), account.getBalance());
    }

    @Test
    public void givenAcountDeletedEmitted_whenHandling_ThenMarkDeleted() throws Exception {
        // arrange
        PowerMockito.doNothing().when(AggregateLifecycle.class, "markDeleted");
        account = new Account();

        // act
        account.on(new AccountDeletedEvent(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.markDeleted();
    }

    @Test
    public void givenAcountUpdatedEmitted_whenHandling_ThenUpdateCustomerId() throws Exception {
        // arrange
        UUID customerId = UUID.randomUUID();
        account = new Account();

        // act
        account.on(new AccountUpdatedEvent(uuid, customerId));

        // assert
        assertEquals(customerId, account.getCustomerId());
    }
}
