package com.ultimatesoftware.banking.transactions.domain.services;

import com.ultimatesoftware.banking.transactions.domain.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.CustomerDto;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionType;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final BankTransactionRepository bankTransactionRepository;
    private final RestService restService;

    public TransactionService(@Autowired BankTransactionRepository bankTransactionRepository, @Autowired RestService restService) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.restService = restService;
    }

    public String transfer(String customerId, UUID accountId, UUID destAccountId, double amount) throws Exception {
        BankAccountDto account = validateCustomerAccount(accountId, customerId);
        BankAccountDto destAccount = getAccount(destAccountId);

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
        BankAccountDto account = validateCustomerAccount(accountId, customerId);

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
        BankAccountDto account = validateCustomerAccount(accountId, customerId);

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

    public BankAccountDto getAccount(UUID accountId) throws NoAccountExistsException {
        try {
            LOG.info(String.format("Fetching account: %s", accountId));
            ResponseEntity<BankAccountDto> response = this.restService.getBankAccount(accountId.toString());
            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        throw new NoAccountExistsException(accountId.toString());
    }

    private CustomerDto getCustomer(String customerId) throws CustomerDoesNotExistException {
        try {
            LOG.info(String.format("Fetching customer: %s", customerId));
            ResponseEntity<CustomerDto> response =  this.restService.getCustomer(customerId);
            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        throw new CustomerDoesNotExistException(String.format("No customer with id %s exists", customerId));
    }

    private void updateAccount(BankTransaction transaction) throws Exception {
        ResponseEntity<BankTransaction> response = this.restService.updateBankTransaction(transaction.getType().toString().toLowerCase(), transaction);
        if (response.getStatusCode() == HttpStatus.OK) {
            return;
        }
        else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            String msg = String.format("No account with id %s exists", transaction.getAccount());
            LOG.warn(msg);
            throw new NoAccountExistsException(msg);
        }
        throw new Exception("There was a problem that occurred when PUTing the transaction to the account service.");
    }

    private void validateAccountBalance(BankAccountDto account, double amount) throws InsufficientBalanceException {
        if (account.getBalance() < amount) {
            String msg = "Insufficient balance on account.";
            LOG.warn(msg);
            throw new InsufficientBalanceException(msg);
        }
    }

    private BankAccountDto validateCustomerAccount(UUID accountId, String customerId) throws NoAccountExistsException, CustomerDoesNotExistException {
        getCustomer(customerId);
        return getAccount(accountId);
    }


    public BankTransaction getTransaction(String transactionId) {
        return this.bankTransactionRepository.findById(transactionId).get();
    }
}
