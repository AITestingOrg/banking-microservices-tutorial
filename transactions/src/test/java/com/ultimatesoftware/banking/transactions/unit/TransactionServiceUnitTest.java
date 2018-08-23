package com.ultimatesoftware.banking.transactions.unit;

import com.ultimatesoftware.banking.transactions.TestConstants;
import com.ultimatesoftware.banking.transactions.domain.eventhandlers.AccountEventHandlers;
import com.ultimatesoftware.banking.transactions.domain.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccount;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.mongodb.client.model.Filters.eq;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceUnitTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @Mock
    protected RestTemplate restTemplate;

    @Test
    public void givenAcountAndCustomerExist_whenDebitingAAValidAmount_thenTheAmountIsDebited() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 0.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    @Test(expected = NoAccountExistsException.class)
    public void givenAccountDoesNotExist_whenDebitingAAValidAmount_thenThrowNoAccountExists() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);

        // Act
        transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
    }

    @Test(expected = CustomerDoesNotExistException.class)
    public void givenCustomerDoesNotExist_whenDebitingAAValidAmount_thenThrowNoCustomerExists() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenThrow(CustomerDoesNotExistException.class);

        // Act
        transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingAAValidAmount_thenTheAmountIsWithdrawed() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 10.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    @Test(expected = NoAccountExistsException.class)
    public void givenNoAcountExist_whenWithdrawingAAValidAmount_thenThrowNoAccountExists() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
    }

    @Test(expected = CustomerDoesNotExistException.class)
    public void givenNoCustomerExist_whenWithdrawingAAValidAmount_thenThrowNoCustomerExists() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenThrow(CustomerDoesNotExistException.class);

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingATheEntireBalance_thenTheAmountIsWithdrawed() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenAcountAndCustomerExist_whenWithdrawingSlightlyMoreThanTheBalance_thenTheAmountIsWithdrawed() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.01);
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingSlightlyLessThanTheBalance_thenTheAmountIsWithdrawed() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 4.99);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingAValidAmount_thenTheAmmountIsTransfered() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 10.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.transfer(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, TestConstants.DESTINATION_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    @Test(expected = NoAccountExistsException.class)
    public void givenNoAcountExist_whenTransferingAValidAmount_thenThrowNoAccountExists() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
    }

    @Test(expected = CustomerDoesNotExistException.class)
    public void givenNoCustomerExist_whenTransferingAValidAmount_thenThrowNoCustomerExists() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenThrow(CustomerDoesNotExistException.class);

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingATheEntireBalance_thenTheAmmountIsTransfered() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenAcountAndCustomerExist_whenTransferingSlightlyMoreThanTheBalance_thenTheAmmountIsTransfered() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.01);
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingSlightlyLessThanTheBalance_thenTheAmmountIsTransfered() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.<BankTransaction>exchange(
                anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<BankTransaction>>any(),
                Matchers.<Class<BankTransaction>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(response);
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<String>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn("");
        when(restTemplate.getForObject(
                anyString(),
                Matchers.<Class<BankAccount>>any(),
                Matchers.<Object>anyVararg()))
                .thenReturn(new BankAccount(TestConstants.ACCOUNT_ID, 5.0, TestConstants.CUSTOMER_ID));

        // Act
        transactionService.withdraw(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 4.99);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }
}
