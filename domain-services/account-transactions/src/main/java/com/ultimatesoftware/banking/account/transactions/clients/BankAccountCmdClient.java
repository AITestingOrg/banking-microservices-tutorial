package com.ultimatesoftware.banking.account.transactions.clients;

import com.ultimatesoftware.banking.account.transactions.models.MessageDto;
import com.ultimatesoftware.banking.account.transactions.models.Transaction;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

@Client(value = "account-cmd", path = "/api/v1/accounts")
public interface BankAccountCmdClient {
    @Put("/credit")
    Flowable<MessageDto> credit(Transaction transaction);
    @Put("/debit")
    Flowable<MessageDto> debit(Transaction transaction);
    @Put("/transfer")
    Flowable<MessageDto> transfer(Transaction transaction);
}
