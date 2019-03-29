package com.ultimatesoftware.banking.account.cmd.rules;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;

import java.math.BigDecimal;

public class StandardAccountRules implements AccountRules {

    public boolean eligibleForDelete(Account account) {
        if (account.getActiveTransfers() == 0 && account.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }

    public boolean eligibleForDebit(Account account, double debitAmount) {
        BigDecimal difference = account.getBalance().subtract(BigDecimal.valueOf(debitAmount));
        if (difference.compareTo(BigDecimal.ZERO) >= 0) {
            return true;
        }
        return false;
    }

    public boolean eligibleForCredit(Account account, double creditAmount) {
        BigDecimal sum = account.getBalance().add(BigDecimal.valueOf(creditAmount));
        if (sum.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) <= 0) {
            return true;
        }
        return false;
    }
}
