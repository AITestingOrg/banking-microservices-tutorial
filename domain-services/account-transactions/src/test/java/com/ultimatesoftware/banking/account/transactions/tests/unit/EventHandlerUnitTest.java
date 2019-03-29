package com.ultimatesoftware.banking.account.transactions.tests.unit;

import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.account.events.*;
import com.ultimatesoftware.banking.account.transactions.eventhandlers.AccountEventHandlers;
import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import com.ultimatesoftware.banking.account.transactions.models.TransactionStatus;
import com.ultimatesoftware.banking.account.transactions.models.TransactionType;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.test.utils.TestConstants;
import io.reactivex.Maybe;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
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
public class EventHandlerUnitTest {

    @InjectMocks
    private AccountEventHandlers accountEventHandlers;

    @Mock
    private Repository<Transaction> bankTransactionRepository;

    @Test
    public void givenAccountTransactionExists_whenAccountCreditedEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(bankTransactionRepository
                .findOne(TestConstants.TRANSACTION_ID.toHexString()))
                .thenReturn(Maybe.just(new Transaction(
                        TestConstants.TRANSACTION_ID,
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID.toHexString(),
                        TestConstants.ACCOUNT_ID.toHexString(),
                        5.0,
                        TestConstants.DESTINATION_ID.toHexString(), null)));
        when(bankTransactionRepository.replaceOne(anyString(), any())).thenReturn(Maybe.just(UpdateResult.acknowledged(1, 1L, null)));

        // Act
        accountEventHandlers.on(
                AccountCreditedEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .customerId(TestConstants.CUSTOMER_ID.toHexString())
                    .creditAmount(5.00)
                    .balance(10.00)
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(1)).replaceOne(eq(TestConstants.TRANSACTION_ID.toHexString()), transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenTransferFailedToStartEvent_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(bankTransactionRepository
                .findOne(TestConstants.TRANSACTION_ID.toHexString()))
                .thenReturn(Maybe.just(new Transaction(
                        TestConstants.TRANSACTION_ID,
                        TransactionType.TRANSFER,
                        TestConstants.CUSTOMER_ID.toString(),
                        TestConstants.ACCOUNT_ID.toString(),
                        5.0,
                        TestConstants.DESTINATION_ID.toString(), null)));
        when(bankTransactionRepository.replaceOne(any(), any())).thenReturn(Maybe.just(UpdateResult.acknowledged(1, 1L, null)));

        // Act
        accountEventHandlers.on(
                TransferFailedToStartEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toString())
                    .transactionId(TestConstants.TRANSACTION_ID.toString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(1)).replaceOne(eq(TestConstants.TRANSACTION_ID.toHexString()), transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenAccountDebitedEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(bankTransactionRepository
                .findOne(TestConstants.TRANSACTION_ID.toHexString()))
                .thenReturn(Maybe.just(new Transaction(
                        TestConstants.TRANSACTION_ID,
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID.toHexString(),
                        TestConstants.ACCOUNT_ID.toHexString(),
                        5.0,
                        TestConstants.DESTINATION_ID.toHexString(), null)));
        when(bankTransactionRepository.replaceOne(anyString(), any())).thenReturn(Maybe.just(UpdateResult.acknowledged(1, 1L, null)));

        // Act
        accountEventHandlers.on(
            AccountDebitedEvent.builder()
                .id(TestConstants.ACCOUNT_ID.toHexString())
                .customerId(TestConstants.CUSTOMER_ID.toHexString())
                .debitAmount(5.00)
                .balance(10.00)
                .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                .build());

        // Assert
        verify(bankTransactionRepository, times(1)).replaceOne(eq(TestConstants.TRANSACTION_ID.toHexString()), transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenTransferCanceledEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(bankTransactionRepository
                .findOne(TestConstants.TRANSACTION_ID.toHexString()))
                .thenReturn(Maybe.just(new Transaction(
                        ObjectId.get(),
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID.toHexString(),
                        TestConstants.ACCOUNT_ID.toHexString(),
                        5.0,
                        TestConstants.DESTINATION_ID.toHexString(), null)));
        when(bankTransactionRepository.replaceOne(any(), any())).thenReturn(Maybe.just(UpdateResult.acknowledged(1, 1L, null)));

        // Act
        accountEventHandlers.on(
                TransferCanceledEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .balance(10.00)
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(1)).replaceOne(eq(TestConstants.TRANSACTION_ID.toHexString()), transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    }

    @Test
    public void givenAccountTransactionExists_whenTransferDepositConcludedEventReceived_thenTheTransactionShouldBeUpdated() {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(bankTransactionRepository
                .findOne(TestConstants.TRANSACTION_ID.toHexString()))
                .thenReturn(Maybe.just(new Transaction(
                        ObjectId.get(),
                        TransactionType.CREDIT,
                        TestConstants.CUSTOMER_ID.toHexString(),
                        TestConstants.ACCOUNT_ID.toHexString(),
                        5.0,
                        TestConstants.DESTINATION_ID.toHexString(), null)));
        when(bankTransactionRepository.replaceOne(any(), any())).thenReturn(Maybe.just(UpdateResult.acknowledged(1, 1L, null)));

        // Act
        accountEventHandlers.on(
                TransferDepositConcludedEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .balance(10.00)
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(1)).replaceOne(eq(TestConstants.TRANSACTION_ID.toHexString()), transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    }

    @Test
    public void givenNoAccountTransactionExists_whenAccountCreditedEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findOne(TestConstants.TRANSACTION_ID.toHexString())).thenReturn(Maybe.never());

        // Act
        accountEventHandlers.on(
            AccountCreditedEvent.builder()
                .id(TestConstants.ACCOUNT_ID.toHexString())
                .customerId(TestConstants.CUSTOMER_ID.toHexString())
                .creditAmount(5.00)
                .balance(10.00)
                .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                .build());

        // Assert
        verify(bankTransactionRepository, times(0)).add(ArgumentMatchers.isA(Transaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenTransferFailedToStartEvent_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findOne(TestConstants.TRANSACTION_ID.toHexString())).thenReturn(Maybe.never());

        // Act
        accountEventHandlers.on(
                TransferFailedToStartEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(0)).add(ArgumentMatchers.isA(Transaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenAccountDebitedEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findOne(TestConstants.TRANSACTION_ID.toHexString())).thenReturn(Maybe.never());

        // Act
        accountEventHandlers.on(
                AccountDebitedEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .customerId(TestConstants.CUSTOMER_ID.toHexString())
                    .debitAmount(5.00)
                    .balance(10.00)
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(0)).add(ArgumentMatchers.isA(Transaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenTransferCanceledEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findOne(TestConstants.TRANSACTION_ID.toHexString())).thenReturn(Maybe.never());

        // Act
        accountEventHandlers.on(
                TransferCanceledEvent.builder()
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .balance(10.00)
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(0)).add(ArgumentMatchers.isA(Transaction.class));
    }

    @Test
    public void givenNoAccountTransactionExists_whenTransferDepositConcludedEventReceived_thenNothingShouldBeDone() {
        // Arrange
        when(bankTransactionRepository.findOne(TestConstants.TRANSACTION_ID.toHexString())).thenReturn(
            Maybe.never());

        // Act
        accountEventHandlers.on(
                TransferCanceledEvent.builder()
                    .id(TestConstants.ACCOUNT_ID.toHexString())
                    .balance(10.00)
                    .transactionId(TestConstants.TRANSACTION_ID.toHexString())
                    .build());

        // Assert
        verify(bankTransactionRepository, times(0)).add(ArgumentMatchers.isA(Transaction.class));
    }
}
