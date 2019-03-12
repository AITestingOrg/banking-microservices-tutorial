package com.ultimatesoftware.banking.transactions.controllers;

import com.ultimatesoftware.banking.transactions.exceptions.BadRequestException;
import com.ultimatesoftware.banking.transactions.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.models.Transaction;
import com.ultimatesoftware.banking.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.transactions.models.TransferTransactionDto;
import com.ultimatesoftware.banking.transactions.services.TransactionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;

@Controller("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Post("/withdraw")
    public HttpResponse<String> withdraw(@Body TransactionDto transactionDto) {
        try {
            validateRequest(transactionDto);
            return HttpResponse.created(transactionService.withdraw(transactionDto));
        } catch (NoAccountExistsException | InsufficientBalanceException | BadRequestException | CustomerDoesNotExistException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.serverError();
        }
    }

    @Post("/deposit")
    public HttpResponse<String> deposit(@Body TransactionDto transactionDto) {
        try {
            validateRequest(transactionDto);
            return HttpResponse.created(transactionService.deposit(transactionDto));
        } catch (BadRequestException | NoAccountExistsException | CustomerDoesNotExistException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.serverError();
        }
    }

    @Post("/transfer")
    public HttpResponse<String> transfer(@Body TransferTransactionDto transactionDto) {
        try {
            return HttpResponse.created(transactionService.transfer(transactionDto));
        } catch (NoAccountExistsException | InsufficientBalanceException | CustomerDoesNotExistException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.serverError();
        }
    }

    @Get("/id/{id}")
    public Transaction getTransaction(@PathVariable("id") String id) {
        Transaction bankTransaction = this.transactionService.getTransaction(id);
        if(bankTransaction != null) {
            return bankTransaction;
        }
        return null;
    }

    private void validateRequest(TransactionDto transactionDto) throws BadRequestException {
        if(transactionDto.getAccountId() == null ||
            transactionDto.getAmount() <= 0 ||
            transactionDto.getCustomerId() == null) {
            throw new BadRequestException();
        }
    }

    private void validateTransferRequest(TransferTransactionDto transactionDto) throws BadRequestException {
        validateRequest(transactionDto);
        if(transactionDto.getDestinationAccountId() == null) {
            throw new BadRequestException();
        }
    }
}
