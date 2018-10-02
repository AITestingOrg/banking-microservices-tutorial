package com.ultimatesoftware.banking.transactions.domain.services;

import com.ultimatesoftware.banking.transactions.domain.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccount;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionType;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class TransactionService extends RestService {
    @Value("${hosts.account-query}")
    private String bankAccountQueryService;
    @Value("${hosts.account-cmd}")
    private String bankAccountCmdService;
    @Value("${hosts.customers}")
    private String bankCustomerService;
    private static final String API_V1_ACCOUNTS = "/api/v1/accounts/";
    private static final String API_V1_CUSTOMERS = "/api/v1/customers/";
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private BankTransactionRepository bankTransactionRepository;

    public TransactionService(@Autowired BankTransactionRepository bankTransactionRepository, @Autowired RestTemplate restTemplate) {
        super(restTemplate);
        this.bankTransactionRepository = bankTransactionRepository;
    }

    public String transfer(String customerId, UUID accountId, UUID destAccountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId, customerId);
        BankAccount destAccount = validateAccount(destAccountId);

        validateAccountBalance(account, amount);

        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(accountId)
                .setType(TransactionType.TRANSFER)
                .setAmount(amount)
                .setCustomerId(customerId)
                .setDestinationAccount(destAccountId)
                .build();
        bankTransactionRepository.save(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    public String withdraw(String customerId, UUID accountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId, customerId);

        validateAccountBalance(account, amount);

        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(accountId)
                .setType(TransactionType.DEBIT)
                .setAmount(amount)
                .setCustomerId(customerId)
                .build();
        bankTransactionRepository.save(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    public String deposit(String customerId, UUID accountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId, customerId);

        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
            .setAccount(accountId)
            .setType(TransactionType.CREDIT)
            .setAmount(amount)
            .setCustomerId(customerId)
            .build();
        bankTransactionRepository.save(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    private void updateAccount(BankTransaction transaction) throws Exception {
        HttpStatus status = put(bankAccountCmdService, API_V1_ACCOUNTS + transaction.getType().toString().toLowerCase(),
                transaction,
                BankTransaction.class);
        if (status.is2xxSuccessful()) {
            return;
        }
        if (status.value() == 404) {
            String msg = String.format("No account with id %s exists", transaction.getAccount());
            LOG.warn(msg);
            throw new NoAccountExistsException(msg);
        }
        throw new Exception("There was a problem that occurred when PUTing the transaction to the account service.");
    }

    private BankAccount getAccount(UUID accountId) {
        try {
            LOG.info(String.format("Fetching account: %s%s", bankAccountQueryService, API_V1_ACCOUNTS + accountId));
            return (BankAccount) get(bankAccountQueryService, API_V1_ACCOUNTS + accountId, BankAccount.class);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return null;
        }
    }

    private void getCustomer(String customerId) throws CustomerDoesNotExistException {
        try {
            LOG.info(String.format("Fetching customer: %s%s", bankCustomerService, API_V1_CUSTOMERS + customerId));
            get(bankCustomerService, API_V1_CUSTOMERS + customerId);
        } catch (HttpClientErrorException e) {
            LOG.warn(e.getMessage());
            throw new CustomerDoesNotExistException(String.format("No customer with id %s exists", customerId));
        }
    }

    private void validateAccountBalance(BankAccount account, double amount) throws InsufficientBalanceException {
        if (account.getBalance() < amount) {
            String msg = "Insufficient balance on account.";
            LOG.warn(msg);
            throw new InsufficientBalanceException(msg);
        }
    }

    private BankAccount validateAccount(UUID accountId, String customerId) throws NoAccountExistsException, CustomerDoesNotExistException {
        validateCustomer(customerId);
        return validateAccount(accountId);
    }

    private BankAccount validateAccount(UUID accountId) throws NoAccountExistsException {
        BankAccount account = getAccount(accountId);
        if (account == null) {
            String msg = String.format("No account with id %s exists", accountId);
            LOG.warn(msg);
            throw new NoAccountExistsException(msg);
        }

        return account;
    }

    private void validateCustomer(String customerId) throws CustomerDoesNotExistException {
        getCustomer(customerId);
    }
}
