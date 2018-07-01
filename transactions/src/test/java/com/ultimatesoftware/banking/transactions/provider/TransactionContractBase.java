package com.ultimatesoftware.banking.transactions.provider;

import com.ultimatesoftware.banking.transactions.TransactionsApplication;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccount;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TransactionsApplication.class)
public abstract class TransactionContractBase {


    @Value("${hosts.account-query}")
    private String bankAccountQueryService;
    @Value("${hosts.account-cmd}")
    private String bankAccountCmdService;
    @Value("${hosts.customers}")
    private String bankCustomerService;
    private static final String API_V1_ACCOUNTS = "/api/v1/accounts/";
    private static final String API_V1_CUSTOMERS = "/api/v1/customers/";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String idCustomer = "12093be5-03a7-43a1-a892-a3f614bc6564";
    private final String idOrigin = "13147684-7d55-476c-ae11-8383407a7f13";
    private final String idDestination = "d99999ff-7324-4467-9468-44d40ca502a1";

    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        BankAccount dummy = new BankAccount(
                UUID.fromString(idOrigin),
                100,
                UUID.fromString(idCustomer));
        BankAccount dummyDestination = new BankAccount(
                UUID.fromString(idDestination),
                100,
                UUID.fromString(idCustomer));
        when(restTemplate.getForObject("http://" + bankAccountQueryService + API_V1_ACCOUNTS + idOrigin, BankAccount.class))
                .thenReturn(dummy);
        when(restTemplate.getForObject("http://" + bankAccountQueryService + API_V1_ACCOUNTS + idDestination, BankAccount.class))
                .thenReturn(dummyDestination);
        when(restTemplate.getForObject("http://" + bankCustomerService + API_V1_CUSTOMERS + idCustomer, String.class))
                .thenReturn("asdasd");
        when(restTemplate.getForObject("http://" + bankCustomerService + API_V1_CUSTOMERS + "a590a8dc-112d-45a3-856c-8bc52c9a2d5d", String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        when(restTemplate.exchange(anyString() , isA(HttpMethod.class), isA(HttpEntity.class), eq(BankTransaction.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @After
    public void teardown() {
    }
}
