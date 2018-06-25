package com.ultimatesoftware.banking.account.cmd.domain.rules;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;

import java.math.BigDecimal;

public class AccountRules {

    public static boolean eligibleForDelete(Account account) {
        if (account.getActiveTransfers() > 0) {
            return false;
        }
        if (account.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }

    public static boolean eligibleForDebit(Account account, double debitAmount) {
        BigDecimal difference = account.getBalance().subtract(BigDecimal.valueOf(debitAmount));
        if (difference.compareTo(BigDecimal.ZERO) >= 0) {
            return true;
        }
        return false;
    }
}
