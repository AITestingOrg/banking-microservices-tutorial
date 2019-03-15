package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForCreditException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDebitException;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import com.ultimatesoftware.banking.events.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountTest {
    @Spy
    private Account account;

    @Mock
    private AccountRules accountRules;

    private static final UUID uuid = UUID.randomUUID();
    private static final String customerId = UUID.randomUUID().toString();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        account.setAccountRules(accountRules);
    }

    @Test
    public void givenAccountIsEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        doNothing().when(account).applyEvent(any());

        account.on(new AccountCreatedEvent(uuid, customerId, 0.0));
        doReturn(true).when(accountRules).eligibleForDelete(any());

        // act
        account.on(new DeleteAccountCommand(uuid));

        // assert
        verify(account, times(1)).applyEvent(any());
    }

    @Test
    public void givenAccountIsNotEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws Exception {
        // arrange
        account.on(new AccountCreatedEvent(uuid, customerId, 50.0));
        doReturn(false).when(accountRules).eligibleForDelete(any());

        // act
        Assertions.assertThrows(AccountNotEligibleForDeleteException.class, () -> {
            account.on(new DeleteAccountCommand(uuid));
        });
    }

    @Test
    public void givenAccountEligibleForDebit_WhenDebiting_AccountDebitedEventEmitted() throws Exception {
        // arrange
        doNothing().when(account).applyEvent(any());
        account.on(new AccountCreatedEvent(uuid, customerId, 50.0));
        doReturn(true).when(accountRules).eligibleForDebit(eq(account), anyDouble());

        // act
        account.on(new DebitAccountCommand(uuid, 49.0, "test"));

        // assert
        verify(account, times(1)).applyEvent(any());
    }

    @Test
    public void givenAccountInEligibleForDebit_WhenDebiting_TransactionFailedEventEmitted() throws Exception {
        // arrange
        doNothing().when(account).applyEvent(any());
        account.on(new AccountCreatedEvent(uuid, customerId, 49.0));
        doReturn(false).when(accountRules).eligibleForDebit(any(), anyDouble());

        // act
        Assertions.assertThrows(AccountNotEligibleForDebitException.class, () -> {
            account.on(new DebitAccountCommand(uuid, 50.0, "test"));
        });
    }

    @Test
    public void givenAccountInEligibleForCredit_WhenCrediting_TransactionFailedIsEmitted() throws Exception {
        // arrange
        doNothing().when(account).applyEvent(any());
        account.on(new AccountCreatedEvent(uuid, customerId, Double.MAX_VALUE));
        doReturn(false).when(accountRules).eligibleForCredit(any(), anyDouble());

        // act
        Assertions.assertThrows(AccountNotEligibleForCreditException.class, () -> {
            account.on(new CreditAccountCommand(uuid, 1.0, "test"));
        });
    }

    @Test
    public void givenAccountEligibleForCredit_WhenCrediting_AccountCreditedIsEmitted() throws Exception {
        // arrange
        doNothing().when(account).applyEvent(any());
        account.on(new AccountCreatedEvent(uuid, customerId, Double.MAX_VALUE - 1.0));
        doReturn(true).when(accountRules).eligibleForCredit(any(), anyDouble());

        // act
        account.on(new CreditAccountCommand(uuid, 2.0, "test"));

        // assert
        verify(account, times(1)).applyEvent(any());
    }

    @Test
    public void givenAccountExists_WhenUpdating_AccountUpdatedIsEmitted() throws Exception {
        // arrange
        doNothing().when(account).applyEvent(any());
        account.on(new AccountCreatedEvent(uuid, customerId, 0.0));

        // act
        account.on(new UpdateAccountCommand(uuid, customerId));

        // assert
        verify(account, times(1)).applyEvent(any());
    }


    @Test
    public void givenAcountCreatedEmitted_whenHandling_ThenUpdateIdBalanceCustomerId() throws Exception {
        // arrange
        String customerId = UUID.randomUUID().toString();

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
        String customerId = UUID.randomUUID().toString();
        account.on(new AccountCreatedEvent(uuid, customerId, 0.0));

        // act
        account.on(new AccountDebitedEvent(uuid, customerId, 10.0, 10.0, "test"));

        // assert
        assertEquals(BigDecimal.valueOf(10.0), account.getBalance());
    }

    @Test
    public void givenAcountCreditedEmitted_whenHandling_ThenUpdateBalance() throws Exception {
        // arrange
        String customerId = UUID.randomUUID().toString();
        account.on(new AccountCreatedEvent(uuid, customerId, 0.0));

        // act
        account.on(new AccountCreditedEvent(uuid, customerId, 10.0, 10.0, "test"));

        // assert
        assertEquals(BigDecimal.valueOf(10.0), account.getBalance());
    }

    @Test
    public void givenAcountDeletedEmitted_whenHandling_ThenMarkDeleted() throws Exception {
        // arrange
        doNothing().when(account).delete();
        account.on(new AccountCreatedEvent(uuid, customerId, 0.0));

        // act
        account.on(new AccountDeletedEvent(uuid));

        // assert
        verify(account, times(1)).delete();
    }

    @Test
    public void givenAcountUpdatedEmitted_whenHandling_ThenUpdateCustomerId() throws Exception {
        // arrange
        String customerId = UUID.randomUUID().toString();
        account.on(new AccountCreatedEvent(uuid, customerId, 20.0));

        // act
        account.on(new AccountUpdatedEvent(uuid, customerId));

        // assert
        assertEquals(customerId, account.getCustomerId());
    }
}
