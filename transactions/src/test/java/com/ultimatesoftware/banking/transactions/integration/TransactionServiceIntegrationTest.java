package com.ultimatesoftware.banking.transactions.integration;

import com.ultimatesoftware.banking.transactions.TransactionsApplication;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;
import com.ultimatesoftware.banking.transactions.service.controllers.ActionsController;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        actionsController = new ActionsController(transactionService);
    }

    @Test
    public void testTransferToExistingAccount_ReturnsSuccess() {
        // Arrange
        BankAccountDto
            originAccount = new BankAccountDto(UUID.randomUUID(), 1000.00d, "originCustomerId");
        BankAccountDto
            destAccount = new BankAccountDto(UUID.randomUUID(), 2500.00d, "destCustomerId");
        double transferAmount = 500.00d;

        configureTransferMocks(originAccount, destAccount);

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
    public void testInsufficientFundTransfer_ReturnsBadRequest() {
        // Arrange
        BankAccountDto
            originAccount = new BankAccountDto(UUID.randomUUID(), 500.00d, "originCustomerId");
        BankAccountDto destAccount = new BankAccountDto(UUID.randomUUID(), 100.00d, "destCustomerId");
        double transferAmount = 1000.00d;

        configureTransferMocks(originAccount, destAccount);

        // Act
        ResponseEntity response = actionsController.transfer(transferAmount, originAccount.getId(),
                originAccount.getCustomerId(), destAccount.getId());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    public void configureTransferMocks(BankAccountDto originAccount, BankAccountDto destAccount) {
        Mockito.when(restTemplate.getForObject("http://" + bankCustomerService + API_V1_CUSTOMERS, String.class))
                .thenReturn(null);
        Mockito.doReturn(originAccount).when(restTemplate).getForObject("http://" + bankAccountQueryService +
                API_V1_ACCOUNTS + originAccount.getId(), BankAccountDto.class);
        Mockito.doReturn(destAccount).when(restTemplate).getForObject("http://" + bankAccountQueryService +
                API_V1_ACCOUNTS + destAccount.getId(), BankAccountDto.class);
    }
}
