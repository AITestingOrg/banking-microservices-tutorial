package com.ultimatesoftware.banking.transactions.unit;

import com.ultimatesoftware.banking.transactions.TestConstants;
import com.ultimatesoftware.banking.transactions.domain.eventhandlers.AccountEventHandlers;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.mongodb.client.model.Filters.eq;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceUnitTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @Mock
    private RestTemplate restTemplate;

    // Given valid accounts

    @Test
    public void whenDebitingAAValidAmount_thenTheAmountIsDebited() throws Exception {
        // Arrange
        ResponseEntity<BankTransaction> response = new ResponseEntity<BankTransaction>(new BankTransaction(), HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                BankTransaction.class))
                .thenReturn(response);

        // Act
        transactionService.deposit(TestConstants.CUSTOMER_ID, TestConstants.ACCOUNT_ID, 5.00);

        // Assert
        verify(bankTransactionRepository, times(1)).save(isA(BankTransaction.class));
    }

    //
}
