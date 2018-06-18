package ultimatesoftware.banking.transactions.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import ultimatesoftware.banking.transactions.domain.models.*;

import java.util.HashMap;
import java.util.UUID;

@Service
public class TransactionService extends RestService {
    @Value("${transactions.bankservice.name}")
    private String BANK_ACCOUNT_SERVICE;
    @Value("${transactions.bankservice.paths.account}")
    private String BANK_ACCOUNT_GET_PATH;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService() {}

    public void transfer(UUID ownerId, UUID accountId, UUID destAccountId, double amount) throws Exception {
        BankAccount account = getAccount(accountId);
        BankAccount destAccount = getAccount(destAccountId);
        if (account == null) {
            String msg = String.format("No account with id %s exists", accountId);
            log.warn(msg);
            throw new NoAccountExistsException(msg);
        }

        if (destAccount == null) {
            String msg = String.format("No destination account with id %s exists", accountId);
            log.warn(msg);
           throw new NoAccountExistsException(msg);
        }

        if (account.getBalance() < amount) {
            String msg = "Insufficient balance on account.";
            log.warn(msg);
            throw new InsufficientBalanceException(msg);
        }
        account.setBalance(account.getBalance() - amount);
        destAccount.setBalance(destAccount.getBalance() + amount);
        boolean success = updateAccount(account.getId(), account);
        if (!success) {
            String msg = String.format("An error occurred when attempting to transfer funds on account %s, transaction cancelled.", accountId);
            log.warn(msg);
            throw new Exception(msg);
        }
        success = updateAccount(destAccount.getId(), destAccount);
        if (!success) {
            updateAccount(account.getId(), account);
            String msg = String.format("An error occurred when attempting to transfer funds on account %s, transaction cancelled.", accountId);
            log.warn(msg);
            throw new Exception(msg);
        }
    }

    public void withdraw(UUID ownerId, UUID accountId, double amount) throws Exception {
        BankAccount account = getAccount(accountId);
        if (account == null) {
            String msg = String.format("No account with id %s exists", accountId);
            log.warn(msg);
            throw new NoAccountExistsException(String.format("No account with id %s exists", accountId));
        }

        if (account.getBalance() < amount) {
            String msg = String.format("An error occurred when attempting to transfer funds on account %s, transaction cancelled.", accountId);
            log.warn(msg);
            throw new InsufficientBalanceException("Insufficient balance on account.");
        }

        account.setBalance(account.getBalance() - amount);
        boolean success = updateAccount(account.getId(), account);
        if (!success) {
            String msg = String.format("An error occurred when attempting to withdraw funds on account %s, transaction cancelled.", accountId);
            log.warn(msg);
            throw new Exception(msg);
        }
    }

    public void deposit(UUID ownerId, UUID accountId, double amount) throws Exception {
        BankAccount account = getAccount(accountId);
        if (account == null) {
            String msg = String.format("No account with id %s exists", accountId);
            log.warn(msg);
            throw new NoAccountExistsException(msg);
        }

        boolean success = updateAccount(account.getId(), account);
        if (!success) {
            String msg = String.format("An error occurred when attempting to withdraw funds on account %s, transaction cancelled.", accountId);
            log.warn(msg);
            throw new Exception(msg);
        }
    }

    protected boolean updateAccount(UUID accountId, BankAccount account) {
        HashMap<String, String> params = new HashMap();
        try {
            return  put(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, account,  BankAccount.class);
        } catch (Exception e) {
            return false;
        }
    }

    protected BankAccount getAccount(UUID accountId) {
        HashMap<String, String> params = new HashMap();
        try {
            return (BankAccount) get(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, BankAccount.class);
        } catch (Exception e) {
            return null;
        }
    }
}
