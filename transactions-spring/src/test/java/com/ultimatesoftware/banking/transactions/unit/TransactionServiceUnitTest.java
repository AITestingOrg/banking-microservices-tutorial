package com.ultimatesoftware.banking.transactions.unit;

import com.ultimatesoftware.banking.transactions.TestConstants;
import com.ultimatesoftware.banking.transactions.domain.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.CustomerDto;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionStatus;
import com.ultimatesoftware.banking.transactions.domain.services.RestService;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceUnitTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @Mock
    protected RestService restService;

    private void buildGetBankAccountMock(BankAccountDto bankAccountDto, HttpStatus status) {
        when(restService.getBankAccount(bankAccountDto.getId().toString())).thenReturn(new ResponseEntity<BankAccountDto>(bankAccountDto, status));
    }

    private void buildGetBankAccountMock(HttpStatus status) {
        when(restService.getBankAccount(ArgumentMatchers.anyString())).thenReturn(new ResponseEntity(status));
    }

    private void buildGetCustomerMock(String customerId, HttpStatus status) {
        when(restService.getCustomer(customerId)).thenReturn(new ResponseEntity(status));
    }

    private void buildTransactionUpdateMock(HttpStatus status) {
        when(restService.updateBankTransaction(ArgumentMatchers.anyString(), ArgumentMatchers.isA(BankTransaction.class))).thenReturn(new ResponseEntity(status));
    }

    @Test
    public void givenAcountAndCustomerExist_whenDebitingAValidAmount_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildTransactionUpdateMock(HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 0.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAccountDoesNotExist_whenDebitingAValidAmount_thenThrowNoAccountExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);

        // Act
        Assertions.assertThrows(NoAccountExistsException.class, () -> {
            transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
        });
    }

    @Test
    public void givenCustomerDoesNotExist_whenDebitingAValidAmount_thenThrowNoCustomerExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.NOT_FOUND);

        // Act
        Assertions.assertThrows(CustomerDoesNotExistException.class, () -> {
            transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingAAValidAmount_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildTransactionUpdateMock(HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 10.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenNoAcountExist_whenWithdrawingAAValidAmount_thenThrowNoAccountExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(HttpStatus.NOT_FOUND);

        // Act
        Assertions.assertThrows(NoAccountExistsException.class, () -> {
                transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
            });

        // Assert
    }

    @Test
    public void givenNoCustomerExist_whenWithdrawingAAValidAmount_thenThrowNoCustomerExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.NOT_FOUND);

        // Act
        Assertions.assertThrows(CustomerDoesNotExistException.class, () -> {
            transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingATheEntireBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildTransactionUpdateMock(HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingSlightlyMoreThanTheBalance_thenTheTransactionIsStarted() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.01);
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingSlightlyLessThanTheBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildTransactionUpdateMock(HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 4.99);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(4.99, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingAValidAmount_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildTransactionUpdateMock(HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID, 10.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, TestConstants.DESTINATION_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(TestConstants.DESTINATION_ID, transaction.getDestinationAccount());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenNoAcountExist_whenTransferingAValidAmount_thenThrowNoAccountExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(HttpStatus.NOT_FOUND);

        // Act
        Assertions.assertThrows(NoAccountExistsException.class, () -> {
                transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID,
                    TestConstants.DESTINATION_ID, 5.00);
            });

        // Assert
    }

    @Test
    public void givenNoCustomerExist_whenTransferingAValidAmount_thenThrowNoCustomerExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.NOT_FOUND);

        // Act
        Assertions.assertThrows(CustomerDoesNotExistException.class, () -> {
            transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID,
                TestConstants.DESTINATION_ID, 5.00);
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingATheEntireBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);
        buildTransactionUpdateMock(HttpStatus.OK);

        // Act
        transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, TestConstants.DESTINATION_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(TestConstants.DESTINATION_ID, transaction.getDestinationAccount());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingSlightlyMoreThanTheBalance_thenTheTransactionIsStarted() {
        // Arrange
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);

        // Act
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID,
                TestConstants.DESTINATION_ID, 5.01);
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingSlightlyLessThanTheBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);
        buildTransactionUpdateMock(HttpStatus.OK);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID, HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID, 5.0, TestConstants.CUSTOMER_ID), HttpStatus.OK);

        // Act
        transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, TestConstants.DESTINATION_ID, 4.99);

        // Assert
        verify(bankTransactionRepository, times(1)).save(transactionCaptor.capture());
        BankTransaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID, transaction.getAccount());
        assertEquals(TestConstants.CUSTOMER_ID, transaction.getCustomerId());
        assertEquals(TestConstants.DESTINATION_ID, transaction.getDestinationAccount());
        assertEquals(4.99, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }
}
