package com.ultimatesoftware.banking.transactions.unit;

import static com.ultimatesoftware.banking.transactions.TestConstants.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@RunWith(MockitoJUnitRunner.class)
public class ActionsControllerUnitTests {
    @InjectMocks
    private ActionsController actionsController;
    @Mock
    private TransactionService transactionService;

    @Test
    public void whenWithdrawIsCalled_thenServiceCalledWithParams() throws Exception {
        // act
        actionsController.withdraw(baseAmount, accountId, customerId);

        // assert
        verify(transactionService, times(1)).withdraw(customerId, accountId, baseAmount);
    }

    @Test
    public void whenWithdrawIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // arrange
        when(transactionService.withdraw(customerId, accountId, baseAmount)).thenReturn(transactionID);

        // act
        ResponseEntity response = actionsController.withdraw(baseAmount, accountId, customerId);

        // assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(transactionID, response.getBody());
    }

    @Test
    public void whenWithdrawIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // arrange
        when(transactionService.withdraw(customerId, accountId, baseAmount)).thenThrow(noAccountExistsException);

        // act
        ResponseEntity response = actionsController.withdraw(baseAmount, accountId, customerId);

        // assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(noAccountExistsException.getMessage(), response.getBody());
    }

    @Test
    public void whenWithdrawIsCalledAndAccountDoesNotHaveEnoughBalance_thenReturnStatusBadRequest() throws Exception {
        // arrange
        when(transactionService.withdraw(customerId, accountId, baseAmount)).thenThrow(insufficientBalanceException);

        // act
        ResponseEntity response = actionsController.withdraw(baseAmount, accountId, customerId);

        // assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(insufficientBalanceException.getMessage(), response.getBody());
    }

    @Test
    public void whenDepositIsCalled_thenServiceCalledWithParams() throws Exception {
        // act
        actionsController.deposit(baseAmount, accountId, customerId);

        // assert
        verify(transactionService, times(1)).deposit(customerId, accountId, baseAmount);
    }

    @Test
    public void whenDepositIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // arrange
        when(transactionService.deposit(customerId, accountId, baseAmount)).thenReturn(transactionID);

        // act
        ResponseEntity response = actionsController.deposit(baseAmount, accountId, customerId);

        // assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(transactionID, response.getBody());
    }

    @Test
    public void whenDepositIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // arrange
        when(transactionService.deposit(customerId, accountId, baseAmount)).thenThrow(noAccountExistsException);

        // act
        ResponseEntity response = actionsController.deposit(baseAmount, accountId, customerId);

        // assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(noAccountExistsException.getMessage(), response.getBody());
    }

    @Test
    public void whenTransferIsCalled_thenServiceCalledWithParams() throws Exception {
        // act
        actionsController.transfer(baseAmount, accountId, customerId, destinationId);

        // assert
        verify(transactionService, times(1)).transfer(customerId, accountId, destinationId, baseAmount);
    }

    @Test
    public void whenTransferIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // arrange
        when(transactionService.transfer(customerId, accountId, destinationId, baseAmount)).thenReturn(transactionID);

        // act
        ResponseEntity response = actionsController.transfer(baseAmount, accountId, customerId, destinationId);

        // assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(transactionID, response.getBody());
    }

    @Test
    public void whenTransferIsCalledAndAccountDoesNotHaveEnoughBalance_thenReturnStatusBadRequest() throws Exception {
        // arrange
        when(transactionService.transfer(customerId, accountId, destinationId, baseAmount))
                .thenThrow(insufficientBalanceException);

        // act
        ResponseEntity response = actionsController.transfer(baseAmount, accountId, customerId, destinationId);

        // assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(insufficientBalanceException.getMessage(), response.getBody());
    }

    @Test
    public void whenTransferIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // arrange
        when(transactionService.transfer(customerId, accountId, destinationId, baseAmount))
                .thenThrow(noAccountExistsException);

        // act
        ResponseEntity response = actionsController.transfer(baseAmount, accountId, customerId, destinationId);

        // assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(noAccountExistsException.getMessage(), response.getBody());
    }
}