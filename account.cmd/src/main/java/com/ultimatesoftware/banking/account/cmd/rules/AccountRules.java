package com.ultimatesoftware.banking.account.cmd.rules;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;

public interface AccountRules {
    boolean eligibleForDelete(Account account);

    boolean eligibleForDebit(Account account, double debitAmount);

    boolean eligibleForCredit(Account account, double creditAmount);
}
