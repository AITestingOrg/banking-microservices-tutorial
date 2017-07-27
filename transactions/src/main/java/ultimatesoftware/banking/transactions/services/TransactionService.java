package ultimatesoftware.banking.transactions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ultimatesoftware.banking.transactions.dao.BankTransactionRepository;
import ultimatesoftware.banking.transactions.models.*;

import java.util.HashMap;

@Service
public class TransactionService extends RestService {
    @Value("${transactions.bankservice.name}")
    private String BANK_ACCOUNT_SERVICE;
    @Value("${transactions.bankservice.paths.account}")
    private String BANK_ACCOUNT_GET_PATH;

    @Autowired
    BankTransactionRepository bankTransactionRepository;

    public TransactionService() {}

    public ActionResult transfer(String ownerId, String accountId, String destAccountId, double amount) {
        BankAccount account = getAccount(accountId);
        BankAccount destAccount = getAccount(destAccountId);
        if(account == null) {
            return new ErrorResult(String.format("No account with id %s exists", accountId));
        }

        if(destAccount == null) {
            return new ErrorResult(String.format("No destination account with id %s exists", accountId));
        }

        if(account.getBalance() < amount) {
            return new ErrorResult("Insufficient balance on account.");
        }
        account.setBalance(account.getBalance() - amount);
        destAccount.setBalance(destAccount.getBalance() + amount);
        boolean success = updateAccount(account.getId(), account);
        if(!success) {
            return new ErrorResult(String.format("An error occurred when attempting to transfer funds, transaction cancelled."));
        }
        success = updateAccount(destAccount.getId(), destAccount);
        if(!success) {
            updateAccount(account.getId(), account);
            return new ErrorResult(String.format("An error occurred when attempting to transfer funds, transaction cancelled.", accountId));
        }
        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(accountId)
                .setType(TransactionType.TRANSFER)
                .setAmount(amount)
                .setDestinationAccount(destAccountId)
                .setCustomerId(ownerId)
                .build();
        bankTransactionRepository.save(transaction);
        return new SuccessResult("Transfer complete");
    }

    public ActionResult withdraw(String ownerId, String accountId, double amount) {
        BankAccount account = getAccount(accountId);
        if(account == null) {
            return new ErrorResult(String.format("No account with id %s exists", accountId));
        }

        if(account.getBalance() < amount) {
            return new ErrorResult("Insufficient balance on account.");
        }

        account.setBalance(account.getBalance() - amount);
        boolean success = updateAccount(account.getId(), account);
        if(!success) {
            return new ErrorResult(String.format("An error occurred when attempting to withdraw funds, transaction cancelled."));
        }
        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(accountId)
                .setType(TransactionType.WITHDRAW)
                .setAmount(amount)
                .setCustomerId(ownerId)
                .build();
        bankTransactionRepository.save(transaction);
        return new SuccessResult("Withdraw complete");
    }

    public ActionResult deposit(String ownerId, String accountId, double amount) {
        BankAccount account = getAccount(accountId);
        if(account == null) {
            return new ErrorResult(String.format("No account with id %s exists", accountId));
        }

        boolean success = updateAccount(account.getId(), account);
        if(!success) {
            return new ErrorResult(String.format("An error occurred when attempting to deposit funds, transaction cancelled."));
        }
        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(accountId)
                .setType(TransactionType.DEPOSIT)
                .setAmount(amount)
                .setCustomerId(ownerId)
                .build();
        bankTransactionRepository.save(transaction);
        return new SuccessResult("Deposit complete");
    }

    protected boolean updateAccount(String accountId, BankAccount account) {
        HashMap<String, String> params = new HashMap();
        try {
            return  put(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, account,  BankAccount.class);
        } catch (Exception e) {
            return false;
        }
    }

    protected BankAccount getAccount(String accountId) {
        HashMap<String, String> params = new HashMap();
        try {
            return (BankAccount) get(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, BankAccount.class);
        } catch (Exception e) {
            return null;
        }
    }
}
