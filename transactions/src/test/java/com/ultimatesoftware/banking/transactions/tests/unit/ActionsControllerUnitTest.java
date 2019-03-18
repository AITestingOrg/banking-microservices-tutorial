package com.ultimatesoftware.banking.transactions.tests.unit;

import com.ultimatesoftware.banking.transactions.controllers.TransactionController;
import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.transactions.models.TransferTransactionDto;
import com.ultimatesoftware.banking.transactions.services.TransactionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ultimatesoftware.banking.transactions.tests.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActionsControllerUnitTest {

    @Mock
    TransactionService transactionService;

    @InjectMocks
    TransactionController actionsController;

    @Test
    public void whenWithdrawIsCalled_thenServiceCalledWithParams() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.withdraw(any())).thenReturn(TRANSACTION_ID.toHexString());

        // Act
        actionsController.withdraw(transactionDto);

        // Assert
        verify(transactionService, times(1)).withdraw(transactionDto);
    }

    @Test
    public void whenWithdrawIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.withdraw(any())).thenReturn(TRANSACTION_ID.toHexString());

        // Act
        HttpResponse<String> response = actionsController.withdraw(transactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.CREATED, response.getStatus());
        Assert.assertEquals(TRANSACTION_ID.toHexString(), response.getBody().get());
    }

    @Test
    public void whenWithdrawIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.withdraw(any())).thenThrow(ACCOUNT_EXISTS_EXCEPTION);

        // Act
        HttpResponse<String> response = actionsController.withdraw(transactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        Assert.assertEquals(Optional.of(ACCOUNT_EXISTS_EXCEPTION.getMessage()), response.getBody());
    }

    @Test
    public void whenWithdrawIsCalledAndAccountDoesNotHaveEnoughBalance_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.withdraw(any())).thenThrow(INSUFFICIENT_BALANCE_EXCEPTION);

        // Act
        HttpResponse<String> response = actionsController.withdraw(transactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        Assert.assertEquals(Optional.of(INSUFFICIENT_BALANCE_EXCEPTION.getMessage()), response.getBody());
    }

    @Test
    public void whenDepositIsCalled_thenServiceCalledWithParams() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.deposit(any())).thenReturn(TRANSACTION_ID.toHexString());

        // Act
        actionsController.deposit(transactionDto);

        // Assert
        verify(transactionService, times(1)).deposit(transactionDto);
    }

    @Test
    public void whenDepositIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.deposit(any())).thenReturn(TRANSACTION_ID.toHexString());

        // Act
        HttpResponse<String> response = actionsController.deposit(transactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.CREATED, response.getStatus());
        Assert.assertEquals(TRANSACTION_ID.toHexString(), response.getBody().get());
    }

    @Test
    public void whenDepositIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT);
        when(transactionService.deposit(any())).thenThrow(ACCOUNT_EXISTS_EXCEPTION);

        // Act
        HttpResponse<String> response = actionsController.deposit(transactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        Assert.assertEquals(Optional.of(ACCOUNT_EXISTS_EXCEPTION.getMessage()), response.getBody());
    }

    @Test
    public void whenTransferIsCalled_thenServiceCalledWithParams() throws Exception {
        // Arrange
        TransferTransactionDto transferTransactionDto = new TransferTransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT, DESTINATION_ID.toHexString());
        when(transactionService.transfer(any())).thenReturn(TRANSACTION_ID.toHexString());
        
        // Act
        actionsController.transfer(transferTransactionDto);

        // Assert
        verify(transactionService, times(1)).transfer(transferTransactionDto);
    }

    @Test
    public void whenTransferIsCalled_thenReturnTransactionIdAndStatusOk() throws Exception {
        // Arrange
        TransferTransactionDto transferTransactionDto = new TransferTransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT, DESTINATION_ID.toHexString());
        when(transactionService.transfer(any())).thenReturn(TRANSACTION_ID.toHexString());

        // Act
        HttpResponse<String> response = actionsController.transfer(transferTransactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.CREATED, response.getStatus());
        Assert.assertEquals(TRANSACTION_ID.toHexString(), response.getBody().get());
    }

    @Test
    public void whenTransferIsCalledAndAccountDoesNotHaveEnoughBalance_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        TransferTransactionDto transferTransactionDto = new TransferTransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT, DESTINATION_ID.toHexString());
        when(transactionService.transfer(any()))
                .thenThrow(INSUFFICIENT_BALANCE_EXCEPTION);

        // Act
        HttpResponse<String> response = actionsController.transfer(transferTransactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        Assert.assertEquals(Optional.of(INSUFFICIENT_BALANCE_EXCEPTION.getMessage()), response.getBody());
    }

    @Test
    public void whenTransferIsCalledAndAccountDoesNotExist_thenReturnStatusBadRequest() throws Exception {
        // Arrange
        TransferTransactionDto transferTransactionDto = new TransferTransactionDto(CUSTOMER_ID.toHexString(), ACCOUNT_ID.toHexString(), BASE_AMOUNT, DESTINATION_ID.toHexString());
        when(transactionService.transfer(any()))
                .thenThrow(ACCOUNT_EXISTS_EXCEPTION);

        // Act
        HttpResponse<String> response = actionsController.transfer(transferTransactionDto);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        Assert.assertEquals(Optional.of(ACCOUNT_EXISTS_EXCEPTION.getMessage()), response.getBody());
    }
}
