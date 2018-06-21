package com.ultimatesoftware.banking.transactions.domain.services;

import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.BankAccount;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.models.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import java.util.UUID;

@Service
public class TransactionService extends RestService {
    @Value("${hosts.account-query}")
    private String BANK_ACCOUNT_QUERY_SERVICE;
    @Value("${hosts.account-cmd}")
    private String BANK_ACCOUNT_CMD_SERVICE;
    private static final String BANK_ACCOUNT_GET_PATH = "/api/v1/accounts/";
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    public TransactionService() {}

    public String transfer(UUID customerId, UUID accountId, UUID destAccountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId);
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

    public String withdraw(UUID customerId, UUID accountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId);

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

    public String deposit(UUID customerId, UUID accountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId);

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

    protected void updateAccount(BankTransaction transaction) throws Exception {
        HttpStatus status = put(BANK_ACCOUNT_CMD_SERVICE, BANK_ACCOUNT_GET_PATH + transaction.getType().toString().toLowerCase(),
                transaction,
                BankTransaction.class);
        if(status.is2xxSuccessful()) {
            return;
        }
        if(status.value() == 404) {
            String msg = String.format("No account with id %s exists", transaction.getAccount());
            log.warn(msg);
            throw new NoAccountExistsException(msg);
        }
        throw new Exception("There was a problem that occured when PUTing the transaction to the account service.");
    }

    protected BankAccount getAccount(UUID accountId) {
        try {
            return (BankAccount) get(BANK_ACCOUNT_QUERY_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, BankAccount.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private void validateAccountBalance(BankAccount account, double amount) throws InsufficientBalanceException {
        if (account.getBalance() < amount) {
            String msg = "Insufficient balance on account.";
            log.warn(msg);
            throw new InsufficientBalanceException(msg);
        }
    }

    private BankAccount validateAccount(UUID accountId) throws NoAccountExistsException {
        BankAccount account = getAccount(accountId);
        if (account == null) {
            String msg = String.format("No account with id %s exists", accountId);
            log.warn(msg);
            throw new NoAccountExistsException(msg);
        }

        return account;
    }
}
