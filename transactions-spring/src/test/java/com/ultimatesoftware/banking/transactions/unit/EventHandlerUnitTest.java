package com.ultimatesoftware.banking.transactions.unit;

import com.ultimatesoftware.banking.events.*;
import com.ultimatesoftware.banking.transactions.TestConstants;
import com.ultimatesoftware.banking.transactions.domain.eventhandlers.AccountEventHandlers;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionStatus;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionType;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("Spring boot 2 mockito2 Junit5 example")
public class EventHandlerUnitTest {

    @InjectMocks
    private AccountEventHandlers accountEventHandlers;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @Test
    public void givenAccountTransactionExists_whenAccountCreditedEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        when(bankTransactionRepository
                .findById(TestConstants.TRANSACTION_ID))
                .thenReturn(Optional.of(new BankTransaction(
                        "1",
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID,
                        TestConstants.ACCOUNT_ID,
                        5.0,
                        TestConstants.DESTINATION_ID)));

        // Act
        accountEventHandlers.on(
                new AccountCreditedEvent(
                        TestConstants.ACCOUNT_ID,
                        TestConstants.CUSTOMER_ID,
                        5.00,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());

        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenTransferFailedToStartEvent_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        when(bankTransactionRepository
                .findById(TestConstants.TRANSACTION_ID))
                .thenReturn(Optional.of(new BankTransaction(
                        "1",
                        TransactionType.TRANSFER,
                        TestConstants.CUSTOMER_ID,
                        TestConstants.ACCOUNT_ID,
                        5.0,
                        TestConstants.DESTINATION_ID)));

        // Act
        accountEventHandlers.on(
                new TransferFailedToStartEvent(
                        TestConstants.ACCOUNT_ID,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());

        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenAccountDebitedEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        when(bankTransactionRepository
                .findById(TestConstants.TRANSACTION_ID))
                .thenReturn(Optional.of(new BankTransaction(
                        "1",
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID,
                        TestConstants.ACCOUNT_ID,
                        5.0,
                        TestConstants.DESTINATION_ID)));

        // Act
        accountEventHandlers.on(
                new AccountDebitedEvent(
                        TestConstants.ACCOUNT_ID,
                        TestConstants.CUSTOMER_ID,
                        5.00,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());

        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenTransferCanceledEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        when(bankTransactionRepository
                .findById(TestConstants.TRANSACTION_ID))
                .thenReturn(Optional.of(new BankTransaction(
                        "1",
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID,
                        TestConstants.ACCOUNT_ID,
                        5.0,
                        TestConstants.DESTINATION_ID)));

        // Act
        accountEventHandlers.on(
                new TransferCanceledEvent(
                        TestConstants.ACCOUNT_ID,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());

        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenTransferDepositConcludedEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        when(bankTransactionRepository
                .findById(TestConstants.TRANSACTION_ID))
                .thenReturn(Optional.of(new BankTransaction(
                        "1",
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID,
                        TestConstants.ACCOUNT_ID,
                        5.0,
                        TestConstants.DESTINATION_ID)));

        // Act
        accountEventHandlers.on(
                new TransferDepositConcludedEvent(
                        TestConstants.ACCOUNT_ID,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());

        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    }

    @Test
    public void givenNoAccountTransactionExists_whenAccountCreditedEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findById(TestConstants.TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act
        accountEventHandlers.on(
                new AccountCreditedEvent(
                        TestConstants.ACCOUNT_ID,
                        TestConstants.CUSTOMER_ID,
                        5.00,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(0)).save(ArgumentMatchers.isA(BankTransaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenTransferFailedToStartEvent_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findById(TestConstants.TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act
        accountEventHandlers.on(
                new TransferFailedToStartEvent(
                        TestConstants.ACCOUNT_ID,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(0)).save(ArgumentMatchers.isA(BankTransaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenAccountDebitedEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findById(TestConstants.TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act
        accountEventHandlers.on(
                new AccountDebitedEvent(
                        TestConstants.ACCOUNT_ID,
                        TestConstants.CUSTOMER_ID,
                        5.00,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(0)).save(ArgumentMatchers.isA(BankTransaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenTransferCanceledEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findById(TestConstants.TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act
        accountEventHandlers.on(
                new TransferCanceledEvent(
                        TestConstants.ACCOUNT_ID,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(0)).save(ArgumentMatchers.isA(BankTransaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenTransferDepositConcludedEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findById(TestConstants.TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act
        accountEventHandlers.on(
                new TransferCanceledEvent(
                        TestConstants.ACCOUNT_ID,
                        10.00,
                        TestConstants.TRANSACTION_ID));

        // Assert
        verify(bankTransactionRepository, times(0)).save(ArgumentMatchers.isA(BankTransaction.class));
    }
}
