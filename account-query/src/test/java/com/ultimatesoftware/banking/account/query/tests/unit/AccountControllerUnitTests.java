package com.ultimatesoftware.banking.account.query.tests.unit;

import com.ultimatesoftware.banking.account.query.controllers.AccountsController;
import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.repository.Repository;
import io.reactivex.Maybe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ultimatesoftware.banking.account.query.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerUnitTests {

    @InjectMocks
    private AccountsController accountController;
    @Mock
    private Repository<Account> mongoRepository;

    @Test
    public void whenGetAccountIsCalled_thenRepositoryFindByAccountIdCalled() {
        // arrange
        when(mongoRepository.findOne(ID.toHexString())).thenReturn(Maybe.just(ACCOUNT));

        // act
        accountController.get(ID.toHexString());

        // verify
        verify(mongoRepository, times(1)).findOne(ID.toHexString());
    }

    @Test
    public void whenGetAccountIsCalledWithExistingId_thenAccountIsRetrievedAndReturnedWithOkStatus() {
        // arrange
        when(mongoRepository.findOne(ID.toHexString())).thenReturn(Maybe.just(ACCOUNT));

        // act
        Account account = accountController.get(ID.toHexString()).blockingGet();

        // verify
        assertTrue(account != null);
    }

    @Test
    public void whenGetAccountIsCalledWithNonExistingId_thenAccountReturnedWithNotFoundStatus() {
        // arrange
        when(mongoRepository.findOne(ID.toHexString())).thenReturn(Maybe.empty());

        // act
        Account account = accountController.get(ID.toHexString()).blockingGet();

        // verify
        assertTrue(account == null);
    }
}
