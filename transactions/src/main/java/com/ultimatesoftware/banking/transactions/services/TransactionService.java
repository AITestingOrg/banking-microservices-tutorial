package com.ultimatesoftware.banking.transactions.services;

import com.ultimatesoftware.banking.transactions.clients.BankAccountClient;
import com.ultimatesoftware.banking.transactions.clients.CustomerClient;
import com.ultimatesoftware.banking.transactions.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.models.CustomerDto;
import com.ultimatesoftware.banking.transactions.models.Transaction;
import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.transactions.models.TransactionType;
import com.ultimatesoftware.banking.transactions.models.TransferTransactionDto;
import com.ultimatesoftware.banking.transactions.respositories.TransactionRepository;
import io.micronaut.http.HttpStatus;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository TransactionRepository;
    private final CustomerClient customerClient;
    private final BankAccountClient bankAccountClient;

    public TransactionService(
        TransactionRepository TransactionRepository, CustomerClient customerClient,
            BankAccountClient bankAccountClient) {
        this.TransactionRepository = TransactionRepository;
        this.customerClient = customerClient;
        this.bankAccountClient = bankAccountClient;
    }

    public String transfer(TransferTransactionDto transferTransactionDto) throws Exception {
        String customerId = transferTransactionDto.getCustomerId();
        UUID accountId = transferTransactionDto.getAccountId();
        Double amount = transferTransactionDto.getAmount();
        UUID destAccountId = transferTransactionDto.getDestinationAccountId();
        BankAccountDto account = validateCustomerAccount(accountId, customerId);
        BankAccountDto destAccount = getAccount(destAccountId);

        validateAccountBalance(account, amount);

        Transaction transaction = new Transaction.TransactionBuilder()
            .setAccount(accountId)
            .setType(TransactionType.TRANSFER)
            .setAmount(amount)
            .setCustomerId(customerId)
            .setDestinationAccount(destAccountId)
            .build();
        TransactionRepository.add(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    public String withdraw(TransactionDto transactionDto) throws Exception {
        String customerId = transactionDto.getCustomerId();
        UUID accountId = transactionDto.getAccountId();
        Double amount = transactionDto.getAmount();
        BankAccountDto account = validateCustomerAccount(accountId, customerId);

        validateAccountBalance(account, amount);

        Transaction transaction = new Transaction.TransactionBuilder()
            .setAccount(accountId)
            .setType(TransactionType.DEBIT)
            .setAmount(amount)
            .setCustomerId(customerId)
            .build();
        TransactionRepository.add(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    public String deposit(TransactionDto transactionDto) throws Exception {
        String customerId = transactionDto.getCustomerId();
        UUID accountId = transactionDto.getAccountId();
        Double amount = transactionDto.getAmount();
        BankAccountDto account = validateCustomerAccount(accountId, customerId);

        Transaction transaction = new Transaction.TransactionBuilder()
            .setAccount(accountId)
            .setType(TransactionType.CREDIT)
            .setAmount(amount)
            .setCustomerId(customerId)
            .build();
        TransactionRepository.add(transaction);

        updateAccount(transaction);

        return transaction.getId();
    }

    public BankAccountDto getAccount(UUID accountId) throws NoAccountExistsException {
        try {
            LOG.info(String.format("Fetching account: %s", accountId));
            BankAccountDto bankAccountDto = bankAccountClient.get(accountId.toString()).blockingGet();
            if( bankAccountDto != null) {
                return bankAccountDto;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        throw new NoAccountExistsException(accountId.toString());
    }

    private CustomerDto getCustomer(String customerId) throws CustomerDoesNotExistException {
        try {
            LOG.info(String.format("Fetching customer: %s", customerId));
            CustomerDto customerDto =  this.customerClient.get(customerId).blockingGet();
            if(customerDto != null) {
                return customerDto;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        throw new CustomerDoesNotExistException(String.format("No customer with id %s exists", customerId));
    }

    private void updateAccount(Transaction transaction) throws Exception {
        if (transaction.getType().equals(TransactionType.CREDIT)) {
            bankAccountClient.credit(transaction).doOnError(throwable -> {

            }).subscribe();
        }
        bankAccountClient.put(transaction).doOnError(error -> {
            if (error instanceof HttpStatus)
        })
        ResponseEntity<Transaction> response = this.restService.updateTransaction(transaction.getType().toString().toLowerCase(), transaction);
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

    private BankAccountDto validateCustomerAccount(UUID accountId, String customerId) throws
        NoAccountExistsException, CustomerDoesNotExistException {
        getCustomer(customerId);
        return getAccount(accountId);
    }


    public Transaction getTransaction(String transactionId) {
        return this.TransactionRepository.findOne(transactionId).blockingGet();
    }
}
