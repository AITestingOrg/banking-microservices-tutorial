package com.ultimatesoftware.banking.account.cmd.domain.rules;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import java.math.BigDecimal;

public interface AccountRules {
    boolean eligibleForDelete(Account account);

    boolean eligibleForDebit(Account account, double debitAmount);

    boolean eligibleForCredit(Account account, double creditAmount);
}
