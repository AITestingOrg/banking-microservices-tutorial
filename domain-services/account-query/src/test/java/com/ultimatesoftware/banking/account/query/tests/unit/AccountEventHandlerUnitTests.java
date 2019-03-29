package com.ultimatesoftware.banking.account.query.tests.unit;

import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.account.events.AccountCreatedEvent;
import com.ultimatesoftware.banking.account.events.AccountCreditedEvent;
import com.ultimatesoftware.banking.account.events.AccountDebitedEvent;
import com.ultimatesoftware.banking.account.events.AccountDeletedEvent;
import com.ultimatesoftware.banking.account.events.AccountUpdatedEvent;
import com.ultimatesoftware.banking.account.events.TransferCanceledEvent;
import com.ultimatesoftware.banking.account.events.TransferDepositConcludedEvent;
import com.ultimatesoftware.banking.account.events.TransferWithdrawConcludedEvent;
import com.ultimatesoftware.banking.account.query.eventhandlers.AccountEventHandler;
import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.repository.Repository;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountEventHandlerUnitTests {
    @InjectMocks
    private AccountEventHandler accountEventHandler;

    @Mock
    private AxonServerConfiguration axonServerConfiguration;

    @Mock
    private Repository<Account> mongoRepository;

    private void setupFindOne(Account account) {
        when(mongoRepository.findOne(account.getHexId())).thenReturn(Maybe.just(account));
    }

    private void setupReplaceOne(Account account) {
        when(mongoRepository.replaceOne(eq(account.getHexId()), any())).thenReturn(Maybe.just(
            UpdateResult.acknowledged(1, 1L, null)));
    }

    @Test
    public void givenAccountCreditedEvent_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(AccountCreditedEvent.builder()
            .id(originalAccount.getHexId())
            .customerId("Test123")
            .creditAmount(10.0)
            .transactionId(transactionId)
            .balance(10.0)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(10.0, captor.getValue().getBalance());
    }

    @Test
    public void givenAccountCreditedEventAndNonZeroPreBalance_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(AccountCreditedEvent.builder()
            .id(originalAccount.getHexId())
            .customerId("Test123")
            .transactionId(transactionId)
            .creditAmount(10.0)
            .balance(1000010.0)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(1000010.0, captor.getValue().getBalance());
    }

    @Test
    public void givenTransferDepositConcludedEvent_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(TransferDepositConcludedEvent.builder()
            .id(originalAccount.getHexId())
            .transactionId(transactionId)
            .balance(150.99)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(150.99, captor.getValue().getBalance());
    }

    @Test
    public void givenAccountDebitedEvent_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(AccountDebitedEvent.builder()
            .id(originalAccount.getHexId())
            .transactionId(transactionId)
            .debitAmount(15.00)
            .balance(30.00)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(30.00, captor.getValue().getBalance());
    }

    @Test
    public void givenAccountDebitedEventAndDebitAmountEqualToBalance_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(AccountDebitedEvent.builder()
            .id(originalAccount.getHexId())
            .transactionId(transactionId)
            .debitAmount(15.00)
            .balance(0.00)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(0.00, captor.getValue().getBalance());
    }

    @Test
    public void givenTransferWithdrawConcludedEvent_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(TransferWithdrawConcludedEvent.builder()
            .id(originalAccount.getHexId())
            .transactionId(transactionId)
            .balance(15.00)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(15.00, captor.getValue().getBalance());
    }

    @Test
    public void givenTransferCanceledEvent_whenHandling_thenAccountBalanceUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        String transactionId = ObjectId.get().toHexString();
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(TransferCanceledEvent.builder()
            .id(originalAccount.getHexId())
            .transactionId(transactionId)
            .balance(15.00)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals(15.00, captor.getValue().getBalance());
    }

    @Test
    public void givenAccountUpdatedEvent_whenHandling_thenAccountOwnerUpdated() {
        // Arrange
        Account originalAccount = new Account(ObjectId.get(), "Test123", 0.0);
        setupFindOne(originalAccount);
        setupReplaceOne(originalAccount);

        // Act
        accountEventHandler.on(AccountUpdatedEvent.builder()
            .id(originalAccount.getHexId())
            .customerId("NotTest123")
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).replaceOne(eq(originalAccount.getHexId()), captor.capture());
        assertEquals("NotTest123", captor.getValue().getCustomerId());
    }

    @Test
    public void givenAccountCreatedEvent_whenHandling_thenAccountCreated() {
        // Arrange
        ObjectId originalAccount = ObjectId.get();
        when(mongoRepository.add(any())).thenReturn(Single.just(new Account(originalAccount, "Test123", 0.0)));

        // Act
        accountEventHandler.on(AccountCreatedEvent.builder()
            .id(originalAccount.toHexString())
            .customerId("NotTest123")
            .balance(0.0)
            .build());

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(mongoRepository, times(1)).add(captor.capture());
        assertEquals("NotTest123", captor.getValue().getCustomerId());
        assertEquals(originalAccount.toHexString(), captor.getValue().getHexId());
        assertEquals(0.0, captor.getValue().getBalance());
    }

    @Test
    public void givenAccountDeletedEvent_whenHandling_thenAccountDeleted() {
        // Arrange
        ObjectId originalAccount = ObjectId.get();
        when(mongoRepository.deleteOne(anyString())).thenReturn(Maybe.empty());

        // Act
        accountEventHandler.on(AccountDeletedEvent.builder()
            .id(originalAccount.toHexString())
            .build());

        // Assert
        verify(mongoRepository, times(1)).deleteOne(originalAccount.toHexString());
    }
}
