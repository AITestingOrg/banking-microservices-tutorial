package com.ultimatesoftware.banking.transactions.unit;

import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;
import com.ultimatesoftware.banking.transactions.service.controllers.ActionsController;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.ultimatesoftware.banking.transactions.TestConstants.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActionsControllerUnitTest {
    @InjectMocks
    private ActionsController actionsController;
    @Mock
    private TransactionService transactionService;

    @Test
    public void whenWithdrawIsCalled_thenServiceCalledWithParams() throws Exception {
        // Arrange

        // Act
        actionsController.withdraw(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        verify(transactionService, times(1)).withdraw(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT);
    }

    @Test
    public void whenWithdrawIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // Arrange
        when(transactionService.withdraw(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT)).thenReturn(TRANSACTION_ID);

        // Act
        ResponseEntity response = actionsController.withdraw(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(TRANSACTION_ID, response.getBody());
    }

    @Test
    public void whenWithdrawIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        when(transactionService.withdraw(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT)).thenThrow(ACCOUNT_EXISTS_EXCEPTION);

        // Act
        ResponseEntity response = actionsController.withdraw(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ACCOUNT_EXISTS_EXCEPTION.getMessage(), response.getBody());
    }

    @Test
    public void whenWithdrawIsCalledAndAccountDoesNotHaveEnoughBalance_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        when(transactionService.withdraw(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT)).thenThrow(INSUFFICIENT_BALANCE_EXCEPTION);

        // Act
        ResponseEntity response = actionsController.withdraw(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(INSUFFICIENT_BALANCE_EXCEPTION.getMessage(), response.getBody());
    }

    @Test
    public void whenDepositIsCalled_thenServiceCalledWithParams() throws Exception {
        // Arrange

        // Act
        actionsController.deposit(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        verify(transactionService, times(1)).deposit(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT);
    }

    @Test
    public void whenDepositIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // Arrange
        when(transactionService.deposit(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT)).thenReturn(TRANSACTION_ID);

        // Act
        ResponseEntity response = actionsController.deposit(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(TRANSACTION_ID, response.getBody());
    }

    @Test
    public void whenDepositIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        when(transactionService.deposit(CUSTOMER_ID, ACCOUNT_ID, BASE_AMOUNT)).thenThrow(ACCOUNT_EXISTS_EXCEPTION);

        // Act
        ResponseEntity response = actionsController.deposit(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ACCOUNT_EXISTS_EXCEPTION.getMessage(), response.getBody());
    }

    @Test
    public void whenTransferIsCalled_thenServiceCalledWithParams() throws Exception {
        // Arrange
        
        // Act
        actionsController.transfer(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID, DESTINATION_ID);

        // Assert
        verify(transactionService, times(1)).transfer(CUSTOMER_ID, ACCOUNT_ID, DESTINATION_ID, BASE_AMOUNT);
    }

    @Test
    public void whenTransferIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // Arrange
        when(transactionService.transfer(CUSTOMER_ID, ACCOUNT_ID, DESTINATION_ID, BASE_AMOUNT)).thenReturn(TRANSACTION_ID);

        // Act
        ResponseEntity response = actionsController.transfer(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID, DESTINATION_ID);

        // Assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(TRANSACTION_ID, response.getBody());
    }

    @Test
    public void whenTransferIsCalledAndAccountDoesNotHaveEnoughBalance_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        when(transactionService.transfer(CUSTOMER_ID, ACCOUNT_ID, DESTINATION_ID, BASE_AMOUNT))
                .thenThrow(INSUFFICIENT_BALANCE_EXCEPTION);

        // Act
        ResponseEntity response = actionsController.transfer(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID, DESTINATION_ID);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(INSUFFICIENT_BALANCE_EXCEPTION.getMessage(), response.getBody());
    }

    @Test
    public void whenTransferIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        when(transactionService.transfer(CUSTOMER_ID, ACCOUNT_ID, DESTINATION_ID, BASE_AMOUNT))
                .thenThrow(ACCOUNT_EXISTS_EXCEPTION);

        // Act
        ResponseEntity response = actionsController.transfer(BASE_AMOUNT, ACCOUNT_ID, CUSTOMER_ID, DESTINATION_ID);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ACCOUNT_EXISTS_EXCEPTION.getMessage(), response.getBody());
    }
}
