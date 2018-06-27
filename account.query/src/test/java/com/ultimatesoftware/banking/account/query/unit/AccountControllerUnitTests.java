package com.ultimatesoftware.banking.account.query.unit;


import com.ultimatesoftware.banking.account.query.domain.models.Account;
import com.ultimatesoftware.banking.account.query.service.controllers.AccountController;
import com.ultimatesoftware.banking.account.query.service.repositories.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerUnitTests {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountRepository accountRepository;

    private final UUID id = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();
    private final Account account = new Account(id, customerId, 0);

    @Test
    public void whenGetAccountIsCalled_thenRepositoryFindByAccountIdCalled() {
        // arrange
        when(accountRepository.findByAccountId(id)).thenReturn(Optional.of(account));

        // act
        accountController.getAccount(id);

        // verify
        verify(accountRepository, times(1)).findByAccountId(id);
    }

    @Test
    public void whenGetAccountIsCalledWithExistingId_thenAccountIsRetrievedAndReturnedWithOkStatus() {
        // arrange
        when(accountRepository.findByAccountId(id)).thenReturn(Optional.of(account));

        // act
        ResponseEntity response = accountController.getAccount(id);

        // verify
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), account);
    }

    @Test
    public void whenGetAccountIsCalledWithNonExistingId_thenAccountReturnedWithNotFoundStatus() {
        // arrange
        when(accountRepository.findByAccountId(id)).thenReturn(Optional.ofNullable(null));

        // act
        ResponseEntity response = accountController.getAccount(id);

        // verify
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
