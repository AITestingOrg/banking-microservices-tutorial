package com.ultimatesoftware.banking.transactions.integration;

import com.netflix.discovery.converters.Auto;
import com.ultimatesoftware.banking.transactions.TransactionsApplication;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccount;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;
import com.ultimatesoftware.banking.transactions.service.controllers.ActionsController;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@SpringBootTest(classes = TransactionsApplication.class)
public class TransactionServiceIntegrationTest {

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    private TransactionService transactionService;

    @Value("${hosts.account-query}")
    private String bankAccountQueryService;
    @Value("${hosts.account-cmd}")
    private String bankAccountCmdService;
    @Value("${hosts.customers}")
    private String bankCustomerService;

    private ActionsController actionsController;

    private static final String API_V1_CUSTOMERS = "/api/v1/customers/";
    private static final String API_V1_ACCOUNTS = "/api/v1/accounts/";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        actionsController = new ActionsController(transactionService);
    }


    @Test
    public void testTransferToExistingAccount_ReturnsSuccess() {
        // Arrange
        BankAccount originAccount = new BankAccount(UUID.randomUUID(), 1000.00d, "originCustomerId");
        BankAccount destAccount = new BankAccount(UUID.randomUUID(), 2500.00d, "destCustomerId");
        double transferAmount = 500.00d;

        Mockito.when(restTemplate.getForObject("http://" + bankCustomerService + API_V1_CUSTOMERS, String.class))
                .thenReturn(null);
        Mockito.doReturn(originAccount).when(restTemplate).getForObject("http://" + bankAccountQueryService +
                        API_V1_ACCOUNTS + originAccount.getId(), BankAccount.class);
        Mockito.doReturn(destAccount).when(restTemplate).getForObject("http://" + bankAccountQueryService +
                        API_V1_ACCOUNTS + destAccount.getId(), BankAccount.class);

        ResponseEntity<BankTransaction> successResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        Mockito.doReturn(successResponseEntity).when(restTemplate)
                .exchange(Mockito.eq("http://" + bankAccountCmdService + API_V1_ACCOUNTS + "transfer"),
                        Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.<Class<BankTransaction>>any());

        // Act
        ResponseEntity response = actionsController.transfer(transferAmount, originAccount.getId(),
                originAccount.getCustomerId(), destAccount.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testInsufficientFundTransfer_ReturnsException() {

    }
}
