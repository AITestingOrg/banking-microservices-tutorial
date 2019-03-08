package com.ultimatesoftware.banking.account.command.rules;

import com.ultimatesoftware.banking.account.command.aggregates.Account;

public interface AccountRules {
    boolean eligibleForDelete(Account account);

    boolean eligibleForDebit(Account account, double debitAmount);

    boolean eligibleForCredit(Account account, double creditAmount);
}
