package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountUpdateDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionDto;
import com.ultimatesoftware.banking.account.cmd.service.controllers.AccountController;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
    protected static final UUID CUSTOMER_ID = UUID.randomUUID();

    @Mock
    protected CommandGateway commandGateway;

    @InjectMocks
    protected AccountController accountController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCorrectParamters_whenCreateEndpointCalled_thenCorrectCommandSent() {
        // arrange
        AccountCreationDto accountCreationDto = new AccountCreationDto(CUSTOMER_ID);

        // act
        UUID uuid = accountController.addAccount(accountCreationDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<CreateAccountCommand>any());
    }

    @Test
    public void givenCorrectParamters_whenUpdateEndpointCalled_thenCorrectCommandSent() {
        // arrange
        UUID id = UUID.randomUUID();
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto(CUSTOMER_ID);

        // act
        accountController.updateAccount(id, accountUpdateDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<UpdateAccountCommand>any());
    }

    @Test
    public void givenCorrectParamters_whenDebitEndpointCalled_thenCorrectCommandSent() {
        // arrange
        TransactionDto transactionDto = new TransactionDto();

        // act
        accountController.debitAccount(transactionDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<DebitAccountCommand>any());
    }

    @Test
    public void givenCorrectParamters_whenCreditEndpointCalled_thenCorrectCommandSent() {
        // arrange
        TransactionDto transactionDto = new TransactionDto();

        // act
        accountController.creditAccount(transactionDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<CreateAccountCommand>any());
    }


    @Test
    public void givenCorrectParamters_whenDeleteEndpointCalled_thenCorrectCommandSent() {
        // arrange
        UUID id = UUID.randomUUID();

        // act
        accountController.deleteAccount(id);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<DeleteAccountCommand>any());
    }

    @Test
    public void givenCorrectParamters_whenTransactionEndpointCalled_thenCorrectCommandSent() {
        // arrange
        TransactionDto transactionDto = new TransactionDto();

        // act
        accountController.startTransaction(transactionDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<StartTransferTransactionCommand>any());
    }
}
