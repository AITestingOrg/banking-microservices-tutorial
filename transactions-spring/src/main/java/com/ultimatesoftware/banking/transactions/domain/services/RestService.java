package com.ultimatesoftware.banking.transactions.domain.services;

import com.ultimatesoftware.banking.transactions.domain.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {
    private static final String API_V1_ACCOUNTS = "/api/v1/accounts/";
    private static final String API_V1_CUSTOMERS = "/api/v1/customers/";
    @Value("${hosts.account-query}")
    private String bankAccountQueryService;
    @Value("${hosts.account-cmd}")
    private String bankAccountCmdService;
    @Value("${hosts.customers}")
    private String bankCustomerService;
    @Value("${hosts.ssl}")
    private String ssl;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;

    public RestService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<BankAccountDto> getBankAccount(String accountId) {
        String url = String.format("%s://%s/%s", ssl, bankAccountQueryService, API_V1_ACCOUNTS, accountId);
        log.info(String.format(String.format("Sending getbankaccount to: %s", url)));
        return restTemplate.exchange(url , HttpMethod.GET, null, BankAccountDto.class);
    }

    public ResponseEntity<CustomerDto> getCustomer(String customerId) {
        String url = String.format("%s://%s/%s", ssl, bankAccountQueryService, API_V1_CUSTOMERS, customerId);
        log.info(String.format(String.format("Sending getcustomer to: %s", url)));
        return restTemplate.exchange(url, HttpMethod.GET, null, CustomerDto.class);
    }

    public ResponseEntity<BankTransaction> updateBankTransaction(String type, BankTransaction objectToPut) {
        String url = String.format("%s://%s/%s", ssl, bankAccountQueryService, API_V1_ACCOUNTS, type);
        HttpEntity<BankTransaction> requestUpdate = new HttpEntity<>(objectToPut);
        log.info(String.format(String.format("Sending updatebanktransaction to: %s", url)));
        return restTemplate.exchange(url , HttpMethod.PUT, requestUpdate, BankTransaction.class);
    }
}
