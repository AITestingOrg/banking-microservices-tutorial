package com.ultimatesoftware.banking.account.transactions.controllers;

import com.ultimatesoftware.banking.account.transactions.exceptions.AccountUpdateException;
import com.ultimatesoftware.banking.account.transactions.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.account.transactions.exceptions.ErrorValidatingBankAccountException;
import com.ultimatesoftware.banking.account.transactions.exceptions.ErrorValidatingCustomerException;
import com.ultimatesoftware.banking.account.transactions.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.account.transactions.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import com.ultimatesoftware.banking.account.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.account.transactions.models.TransferTransactionDto;
import com.ultimatesoftware.banking.account.transactions.services.TransactionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.validation.Valid;

@Controller("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Post("/withdraw")
    public HttpResponse<String> withdraw(@Valid @Body TransactionDto transactionDto) {
        try {
            return HttpResponse.created(transactionService.withdraw(transactionDto));
        } catch (NoAccountExistsException | InsufficientBalanceException | CustomerDoesNotExistException | ErrorValidatingBankAccountException | ErrorValidatingCustomerException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (AccountUpdateException e) {
            return HttpResponse.serverError();
        }
    }

    @Post("/deposit")
    public HttpResponse<String> deposit(@Valid @Body TransactionDto transactionDto) {
        try {
            return HttpResponse.created(transactionService.deposit(transactionDto));
        } catch (NoAccountExistsException | CustomerDoesNotExistException | ErrorValidatingBankAccountException | ErrorValidatingCustomerException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (AccountUpdateException e) {
            return HttpResponse.serverError();
        }
    }

    @Post("/transfer")
    public HttpResponse<String> transfer(@Body TransferTransactionDto transactionDto) {
        try {
            return HttpResponse.created(transactionService.transfer(transactionDto));
        } catch (NoAccountExistsException | InsufficientBalanceException | CustomerDoesNotExistException | ErrorValidatingBankAccountException | ErrorValidatingCustomerException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (AccountUpdateException e) {
            return HttpResponse.serverError();
        }
    }

    @Get("/id/{id}")
    public Transaction getTransaction(String id) {
        Transaction bankTransaction = this.transactionService.getTransaction(id);
        if (bankTransaction != null) {
            return bankTransaction;
        }
        return null;
    }
}
