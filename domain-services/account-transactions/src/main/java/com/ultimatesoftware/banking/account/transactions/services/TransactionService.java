package com.ultimatesoftware.banking.account.transactions.services;

import com.ultimatesoftware.banking.account.transactions.clients.*;
import com.ultimatesoftware.banking.account.transactions.exceptions.*;
import com.ultimatesoftware.banking.account.transactions.models.*;
import com.ultimatesoftware.banking.api.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final Repository<Transaction> transactionRepository;
    private final PeopleDetailsClient peopleDetailsClient;
    private final BankAccountQueryClient bankAccountQueryClient;
    private final BankAccountCmdClient bankAccountCmdClient;

    public TransactionService(
        Repository<Transaction> transactionRepository, PeopleDetailsClient peopleDetailsClient,
            BankAccountQueryClient bankAccountQueryClient, BankAccountCmdClient bankAccountCmdClient) {
        this.transactionRepository = transactionRepository;
        this.peopleDetailsClient = peopleDetailsClient;
        this.bankAccountQueryClient = bankAccountQueryClient;
        this.bankAccountCmdClient = bankAccountCmdClient;
    }

    public String transfer(TransferTransactionDto transferTransactionDto)
        throws CustomerDoesNotExistException, ErrorValidatingBankAccountException,
        NoAccountExistsException, ErrorValidatingCustomerException, InsufficientBalanceException,
        AccountUpdateException {
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
            .destinationAccountId(destAccountId)
            .build();

        transactionRepository.add(transaction).blockingGet();

        updateAccount(transaction);

        return transaction.getHexId();
    }

    public String withdraw(TransactionDto transactionDto)
        throws CustomerDoesNotExistException, ErrorValidatingBankAccountException,
        NoAccountExistsException, ErrorValidatingCustomerException, InsufficientBalanceException,
        AccountUpdateException {
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

    public String deposit(TransactionDto transactionDto)
        throws CustomerDoesNotExistException, ErrorValidatingBankAccountException,
        NoAccountExistsException, ErrorValidatingCustomerException, AccountUpdateException {
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
            if (bankAccountDto != null) {
                return bankAccountDto;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ErrorValidatingBankAccountException("An unknown error occurred while attempting to validate the account.");
        }
        throw new NoAccountExistsException(String.format("No account with id %s exists", accountId));
    }

    private PersonDetailsDto getCustomer(String customerId)
        throws CustomerDoesNotExistException, ErrorValidatingCustomerException {
        try {
            LOG.info(String.format("Fetching customer: %s", customerId));
            PersonDetailsDto customer =  this.peopleDetailsClient.get(customerId).blockingGet();
            if (customer != null) {
                return customer;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ErrorValidatingCustomerException("An unknown error occurred while attempting to validate the account owner.");
        }
        throw new CustomerDoesNotExistException(String.format("No person with id %s exists", customerId));
    }

    private void updateAccount(Transaction transaction) throws AccountUpdateException {
        if (transaction.getType().equals(TransactionType.CREDIT)) {
            bankAccountCmdClient.credit(transaction).blockingFirst();
        } else if (transaction.getType().equals(TransactionType.DEBIT)) {
            bankAccountCmdClient.debit(transaction).blockingFirst();
        } else if (transaction.getType().equals(TransactionType.TRANSFER)) {
            bankAccountCmdClient.transfer(transaction).blockingFirst();
        } else {
            String msg = "An unknown error occurred during account update.";
            LOG.error(msg);
            throw new AccountUpdateException(msg);
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
