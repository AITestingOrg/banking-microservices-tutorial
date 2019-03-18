package com.ultimatesoftware.banking.transactions.services;

import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.transactions.clients.BankAccountCmdClient;
import com.ultimatesoftware.banking.transactions.clients.BankAccountQueryClient;
import com.ultimatesoftware.banking.transactions.exceptions.ErrorValidatingBankAccountException;
import com.ultimatesoftware.banking.transactions.exceptions.ErrorValidatingCustomerException;
import com.ultimatesoftware.banking.transactions.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.models.Transaction;
import com.ultimatesoftware.banking.transactions.clients.CustomerClient;
import com.ultimatesoftware.banking.transactions.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.models.BankAccountDto;
import com.ultimatesoftware.banking.transactions.models.CustomerDto;
import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.transactions.models.TransactionType;
import com.ultimatesoftware.banking.transactions.models.TransferTransactionDto;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final Repository<Transaction> transactionRepository;
    private final CustomerClient customerClient;
    private final BankAccountQueryClient bankAccountQueryClient;
    private final BankAccountCmdClient bankAccountCmdClient;

    public TransactionService(
        Repository<Transaction> transactionRepository, CustomerClient customerClient,
            BankAccountQueryClient bankAccountQueryClient, BankAccountCmdClient bankAccountCmdClient) {
        this.transactionRepository = transactionRepository;
        this.customerClient = customerClient;
        this.bankAccountQueryClient = bankAccountQueryClient;
        this.bankAccountCmdClient = bankAccountCmdClient;
    }

    public String transfer(TransferTransactionDto transferTransactionDto) throws Exception {
        String customerId = transferTransactionDto.getCustomerId();
        String accountId = transferTransactionDto.getAccountId();
        Double amount = transferTransactionDto.getAmount();
        String destAccountId = transferTransactionDto.getDestinationAccountId();
        BankAccountDto account = validateCustomerAccount(accountId, customerId);
        BankAccountDto destAccount = getAccount(destAccountId);

        validateAccountBalance(account, amount);

        Transaction transaction = Transaction.builder()
            .accountId(accountId)
            .type(TransactionType.TRANSFER)
            .amount(amount)
            .customerId(customerId)
            .destinationAccount(destAccountId)
            .build();

        transactionRepository.add(transaction).blockingGet();

        updateAccount(transaction);

        return transaction.getHexId();
    }

    public String withdraw(TransactionDto transactionDto) throws Exception {
        String customerId = transactionDto.getCustomerId();
        String accountId = transactionDto.getAccountId();
        Double amount = transactionDto.getAmount();
        BankAccountDto account = validateCustomerAccount(accountId, customerId);

        validateAccountBalance(account, amount);

        Transaction transaction = Transaction.builder()
            .accountId(accountId)
            .type(TransactionType.DEBIT)
            .amount(amount)
            .customerId(customerId)
            .build();

        transactionRepository.add(transaction).blockingGet();

        updateAccount(transaction);

        return transaction.getHexId();
    }

    public String deposit(TransactionDto transactionDto) throws Exception {
        String customerId = transactionDto.getCustomerId();
        String accountId = transactionDto.getAccountId();
        Double amount = transactionDto.getAmount();
        BankAccountDto account = validateCustomerAccount(accountId, customerId);

        Transaction transaction = Transaction.builder()
            .accountId(accountId)
            .type(TransactionType.CREDIT)
            .amount(amount)
            .customerId(customerId)
            .build();

        transactionRepository.add(transaction).blockingGet();

        updateAccount(transaction);

        return transaction.getHexId();
    }

    public BankAccountDto getAccount(String accountId)
        throws NoAccountExistsException, ErrorValidatingBankAccountException {
        try {
            LOG.info(String.format("Fetching account: %s", accountId));
            BankAccountDto bankAccountDto = bankAccountQueryClient.get(accountId).blockingGet();
            if( bankAccountDto != null) {
                return bankAccountDto;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ErrorValidatingBankAccountException("An unknown error occurred while attempting to validate the account.");
        }
        throw new NoAccountExistsException(String.format("No account with id %s exists", accountId));
    }

    private CustomerDto getCustomer(String customerId)
        throws CustomerDoesNotExistException, ErrorValidatingCustomerException {
        try {
            LOG.info(String.format("Fetching customer: %s", customerId));
            CustomerDto customer =  this.customerClient.get(customerId).blockingGet();
            if (customer != null) {
                return customer;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ErrorValidatingCustomerException("An unknown error occurred while attempting to validate the customer.");
        }
        throw new CustomerDoesNotExistException(String.format("No customer with id %s exists", customerId));
    }

    private void updateAccount(Transaction transaction) throws Exception {
        if (transaction.getType().equals(TransactionType.CREDIT)) {
            bankAccountCmdClient.credit(transaction).blockingFirst();
        }
        else if (transaction.getType().equals(TransactionType.DEBIT)) {
            bankAccountCmdClient.debit(transaction).blockingFirst();
        }
        else if (transaction.getType().equals(TransactionType.TRANSFER)) {
            bankAccountCmdClient.transfer(transaction).blockingFirst();
        } else {
            throw new Exception("An unknown error occured.");
        }
    }

    private void validateAccountBalance(BankAccountDto account, double amount) throws
        InsufficientBalanceException {
        if (account.getBalance() < amount) {
            String msg = "Insufficient balance on account.";
            LOG.warn(msg);
            throw new InsufficientBalanceException(msg);
        }
    }

    private BankAccountDto validateCustomerAccount(String accountId, String customerId) throws
        NoAccountExistsException, CustomerDoesNotExistException, ErrorValidatingCustomerException,
        ErrorValidatingBankAccountException {
        getCustomer(customerId);
        return getAccount(accountId);
    }


    public Transaction getTransaction(String transactionId) {
        return this.transactionRepository.findOne(transactionId).blockingGet();
    }
}
