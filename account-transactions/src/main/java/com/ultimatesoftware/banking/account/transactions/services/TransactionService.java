package com.ultimatesoftware.banking.account.transactions.services;

import com.ultimatesoftware.banking.account.transactions.clients.PeopleDetailsClient;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.account.transactions.clients.BankAccountCmdClient;
import com.ultimatesoftware.banking.account.transactions.clients.BankAccountQueryClient;
import com.ultimatesoftware.banking.account.transactions.exceptions.*;
import com.ultimatesoftware.banking.account.transactions.models.*;
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
            throw new ErrorValidatingCustomerException("An unknown error occurred while attempting to validate the customer.");
        }
        throw new CustomerDoesNotExistException(String.format("No customer with id %s exists", customerId));
    }

    private void updateAccount(Transaction transaction) throws Exception {
        if (transaction.getType().equals(TransactionType.CREDIT)) {
            bankAccountCmdClient.credit(transaction).blockingFirst();
        } else if (transaction.getType().equals(TransactionType.DEBIT)) {
            bankAccountCmdClient.debit(transaction).blockingFirst();
        } else if (transaction.getType().equals(TransactionType.TRANSFER)) {
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
