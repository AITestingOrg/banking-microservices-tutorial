package com.ultimatesoftware.banking.transactions.unit;

import com.ultimatesoftware.banking.transactions.domain.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.services.RestService;
import java.net.URI;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;

public class RestServiceUnitTest {
    @InjectMocks
    protected RestService restService;

    @Mock
    protected RestTemplate restTemplate;

    private OngoingStubbing buildMockExchangeTemplate() {
        return when(restTemplate.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.eq(HttpMethod.PUT),
            ArgumentMatchers.<HttpEntity>any(),
            ArgumentMatchers.<Class<BankTransaction>>any()));
    }

    private OngoingStubbing buildMockGetStringTemplate() {
        return when(restTemplate.getForObject(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.<Class<String>>any()));
    }

    private OngoingStubbing buildMockGetBankAccountTemplate() {
        return when(restTemplate.getForObject(
            ArgumentMatchers.any(URI.class),
            ArgumentMatchers.<Class<BankAccountDto>>any()));
    }
}

