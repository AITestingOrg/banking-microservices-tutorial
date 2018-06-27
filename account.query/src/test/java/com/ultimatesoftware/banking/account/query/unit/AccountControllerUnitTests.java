package com.ultimatesoftware.banking.account.query.unit;

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

import static com.ultimatesoftware.banking.account.query.utils.TestConstants.ACCOUNT;
import static com.ultimatesoftware.banking.account.query.utils.TestConstants.ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerUnitTests {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountRepository accountRepository;

    @Test
    public void whenGetAccountIsCalled_thenRepositoryFindByAccountIdCalled() {
        // arrange
        when(accountRepository.findByAccountId(ID)).thenReturn(Optional.of(ACCOUNT));

        // act
        accountController.getAccount(ID);

        // verify
        verify(accountRepository, times(1)).findByAccountId(ID);
    }

    @Test
    public void whenGetAccountIsCalledWithExistingId_thenAccountIsRetrievedAndReturnedWithOkStatus() {
        // arrange
        when(accountRepository.findByAccountId(ID)).thenReturn(Optional.of(ACCOUNT));

        // act
        ResponseEntity response = accountController.getAccount(ID);

        // verify
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), ACCOUNT);
    }

    @Test
    public void whenGetAccountIsCalledWithNonExistingId_thenAccountReturnedWithNotFoundStatus() {
        // arrange
        when(accountRepository.findByAccountId(ID)).thenReturn(Optional.ofNullable(null));

        // act
        ResponseEntity response = accountController.getAccount(ID);

        // verify
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
