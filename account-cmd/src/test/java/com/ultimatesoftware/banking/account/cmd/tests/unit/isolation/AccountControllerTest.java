package com.ultimatesoftware.banking.account.cmd.tests.unit.isolation;

import com.ultimatesoftware.banking.account.cmd.commands.CreateAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.DebitAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.DeleteAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.StartTransferTransactionCommand;
import com.ultimatesoftware.banking.account.cmd.controllers.AccountsController;
import com.ultimatesoftware.banking.account.cmd.models.AccountDto;
import com.ultimatesoftware.banking.account.cmd.models.TransactionDto;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    protected static final String CUSTOMER_ID = UUID.randomUUID().toString();

    @Mock
    protected CommandGateway commandGateway;

    @InjectMocks
    protected AccountsController accountsController;

    @Test
    public void givenCorrectParameters_whenCreateEndpointCalled_thenCorrectCommandSent() {
        // arrange
        AccountDto accountCreationDto = new AccountDto(CUSTOMER_ID);

        // act
        String id = accountsController.create(accountCreationDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<CreateAccountCommand>any());
    }

    @Test
    public void givenCorrectParameters_whenDebitEndpointCalled_thenCorrectCommandSent() {
        // arrange
        String id = ObjectId.get().toHexString();
        TransactionDto transactionDto = new TransactionDto(id, id, id, 50.0, id);

        // act
        accountsController.debit(transactionDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<DebitAccountCommand>any());
    }

    @Test
    public void givenCorrectParameters_whenCreditEndpointCalled_thenCorrectCommandSent() {
        // arrange
        String id = ObjectId.get().toHexString();
        TransactionDto transactionDto = new TransactionDto(id, id, id, 50.0, id);

        // act
        accountsController.credit(transactionDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<CreateAccountCommand>any());
    }


    @Test
    public void givenCorrectParameters_whenDeleteEndpointCalled_thenCorrectCommandSent() {
        // arrange
        String id = ObjectId.get().toHexString();

        // act
        accountsController.delete(id);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<DeleteAccountCommand>any());
    }

    @Test
    public void givenCorrectParameters_whenTransactionEndpointCalled_thenCorrectCommandSent() {
        // arrange
        String id = ObjectId.get().toHexString();
        TransactionDto transactionDto = new TransactionDto(id, id, id, 50.0, id);

        // act
        accountsController.transfer(transactionDto);

        // assert
        Mockito.verify(commandGateway).send(Matchers.<StartTransferTransactionCommand>any());
    }
}
