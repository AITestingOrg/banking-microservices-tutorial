package com.ultimatesoftware.banking.transactions.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.*;
import com.ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

import java.util.UUID;

@Service
public class TransactionService extends RestService {
    private static final String BANK_ACCOUNT_SERVICE = "accounts";
    private static final String BANK_ACCOUNT_GET_PATH = "/api/account/";
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    public TransactionService() {}

    public UUID transfer(UUID customerId, UUID accountId, UUID destAccountId, double amount) throws Exception {
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

    public UUID withdraw(UUID customerId, UUID accountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId);

        validateAccountBalance(account, amount);

        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(accountId)
                .setType(TransactionType.WITHDRAW)
                .setAmount(amount)
                .setCustomerId(customerId)
                .build();
        bankTransactionRepository.save(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    public UUID deposit(UUID customerId, UUID accountId, double amount) throws Exception {
        BankAccount account = validateAccount(accountId);

        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
            .setAccount(accountId)
            .setType(TransactionType.DEPOSIT)
            .setAmount(amount)
            .setCustomerId(customerId)
            .build();
        bankTransactionRepository.save(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    protected void updateAccount(BankTransaction transaction) {
        put(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + transaction.getType().toString().toLowerCase(), transaction,  BankTransaction.class);
    }

    protected BankAccount getAccount(UUID accountId) {
        try {
            return (BankAccount) get(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, BankAccount.class);
        } catch (Exception e) {
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
