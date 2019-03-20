package com.ultimatesoftware.banking.transactions.tests.unit;

import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.transactions.clients.BankAccountCmdClient;
import com.ultimatesoftware.banking.transactions.clients.BankAccountQueryClient;
import com.ultimatesoftware.banking.transactions.clients.CustomerClient;
import com.ultimatesoftware.banking.transactions.exceptions.*;
import com.ultimatesoftware.banking.transactions.models.*;
import com.ultimatesoftware.banking.transactions.services.TransactionService;
import com.ultimatesoftware.banking.transactions.tests.TestConstants;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceUnitTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private Repository<Transaction> bankTransactionRepository;

    @Mock
    protected CustomerClient customerClient;

    @Mock
    protected BankAccountQueryClient accountQueryClient;

    @Mock
    protected BankAccountCmdClient accountCmdClient;

    private void buildGetBankAccountMock(BankAccountDto bankAccountDto) {
        when(accountQueryClient.get(bankAccountDto.getId())).thenReturn(Maybe.just(bankAccountDto));
    }

    private void buildGetNoBankAccountMock() {
        when(accountQueryClient.get(ArgumentMatchers.anyString())).thenReturn(Maybe.empty());
    }

    private void buildGetCustomerMock(String customerId) {
        when(customerClient.get(customerId)).thenReturn(Maybe.just(new CustomerDto(customerId, "", "")));
    }

    private void buildGetNoCustomerMock() {
        when(customerClient.get(any())).thenReturn(Maybe.empty());
    }

    private void buildTransactionCreditMock() {
        String success = "Success";
        when(accountCmdClient.credit(any())).thenReturn(Flowable.just(new MessageDto(success)));
    }

    private void buildTransactionDebitMock() {
        String success = "Success";
        when(accountCmdClient.debit(any())).thenReturn(Flowable.just(new MessageDto(success)));
    }

    private void buildTransactionTransferMock() {
        String success = "Success";
        when(accountCmdClient.transfer(any())).thenReturn(Flowable.just(new MessageDto(success)));
    }

    private void buildRepositoryAddMock() {
        when(bankTransactionRepository.add(any())).thenReturn(Single.just(new Transaction(
            TestConstants.TRANSACTION_ID,
            TransactionType.TRANSFER,
            TestConstants.ACCOUNT_ID.toHexString(),
            TestConstants.CUSTOMER_ID.toHexString(),
            TestConstants.BASE_AMOUNT,
            TestConstants.DESTINATION_ID.toHexString(),
            TransactionStatus.IN_PROGRESS)));
    }

    @Test
    public void givenAcountAndCustomerExist_whenDebitingAValidAmount_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildTransactionCreditMock();
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 0.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildRepositoryAddMock();

        // Act
        transactionService.deposit(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAccountDoesNotExist_whenDebitingAValidAmount_thenThrowNoAccountExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());

        // Act
        Assertions.assertThrows(ErrorValidatingBankAccountException.class, () -> {
            transactionService.deposit(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));
        });
    }

    @Test
    public void givenCustomerDoesNotExist_whenDebitingAValidAmount_thenThrowNoCustomerExists() {
        // Arrange
        buildGetNoCustomerMock();

        // Act
        Assertions.assertThrows(CustomerDoesNotExistException.class, () -> {
            transactionService.deposit(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingAAValidAmount_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildTransactionDebitMock();
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 10.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildRepositoryAddMock();

        // Act
        transactionService.withdraw(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenNoAcountExist_whenWithdrawingAAValidAmount_thenThrowNoAccountExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetNoBankAccountMock();

        // Act
        Assertions.assertThrows(NoAccountExistsException.class, () -> {
                transactionService.withdraw(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));
            });

        // Assert
    }

    @Test
    public void givenNoCustomerExist_whenWithdrawingAAValidAmount_thenThrowNoCustomerExists() {
        // Arrange
        buildGetNoCustomerMock();

        // Act
        Assertions.assertThrows(CustomerDoesNotExistException.class, () -> {
            transactionService.withdraw(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingATheEntireBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildTransactionDebitMock();
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildRepositoryAddMock();

        // Act
        transactionService.withdraw(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingSlightlyMoreThanTheBalance_thenTheTransactionIsStarted() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));

        // Act
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.withdraw(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.01));
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenWithdrawingSlightlyLessThanTheBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildTransactionDebitMock();
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildRepositoryAddMock();

        // Act
        transactionService.withdraw(new TransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 4.99));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(4.99, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingAValidAmount_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildTransactionTransferMock();
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID.toHexString(), 10.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildRepositoryAddMock();

        // Act
        transactionService.transfer(new TransferTransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00, TestConstants.DESTINATION_ID.toHexString()));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(TestConstants.DESTINATION_ID.toHexString(), transaction.getDestinationAccount());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenNoAcountExist_whenTransferingAValidAmount_thenThrowNoAccountExists() {
        // Arrange
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetNoBankAccountMock();

        // Act
        Assertions.assertThrows(NoAccountExistsException.class, () -> {
                transactionService.transfer(new TransferTransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(),
                    5.00, TestConstants.DESTINATION_ID.toHexString()));
            });

        // Assert
    }

    @Test
    public void givenNoCustomerExist_whenTransferingAValidAmount_thenThrowNoCustomerExists() {
        // Arrange
        buildGetNoCustomerMock();

        // Act
        Assertions.assertThrows(CustomerDoesNotExistException.class, () -> {
            transactionService.transfer(new TransferTransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00,
                TestConstants.DESTINATION_ID.toHexString()));
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingATheEntireBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildTransactionTransferMock();
        buildRepositoryAddMock();

        // Act
        transactionService.transfer(new TransferTransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.00, TestConstants.DESTINATION_ID.toHexString()));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(TestConstants.DESTINATION_ID.toHexString(), transaction.getDestinationAccount());
        assertEquals(5.00, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingSlightlyMoreThanTheBalance_thenTheTransactionIsStarted() {
        // Arrange
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());

        // Act
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.transfer(new TransferTransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 5.01,
                TestConstants.DESTINATION_ID.toHexString()));
        });
    }

    @Test
    public void givenAcountAndCustomerExist_whenTransferingSlightlyLessThanTheBalance_thenTheTransactionIsStarted()
        throws Exception {
        // Arrange
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        buildTransactionTransferMock();
        buildGetCustomerMock(TestConstants.CUSTOMER_ID.toHexString());
        buildGetBankAccountMock(new BankAccountDto(TestConstants.ACCOUNT_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildGetBankAccountMock(new BankAccountDto(TestConstants.DESTINATION_ID.toHexString(), 5.0, TestConstants.CUSTOMER_ID.toHexString()));
        buildRepositoryAddMock();

        // Act
        transactionService.transfer(new TransferTransactionDto(TestConstants.CUSTOMER_ID.toHexString(), TestConstants.ACCOUNT_ID.toHexString(), 4.99, TestConstants.DESTINATION_ID.toHexString()));

        // Assert
        verify(bankTransactionRepository, times(1)).add(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(TestConstants.ACCOUNT_ID.toHexString(), transaction.getAccountId());
        assertEquals(TestConstants.CUSTOMER_ID.toHexString(), transaction.getCustomerId());
        assertEquals(TestConstants.DESTINATION_ID.toHexString(), transaction.getDestinationAccount());
        assertEquals(4.99, transaction.getAmount(), 0.001);
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }
}
