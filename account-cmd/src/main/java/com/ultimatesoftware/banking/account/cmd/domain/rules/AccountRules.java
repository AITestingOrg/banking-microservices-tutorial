package com.ultimatesoftware.banking.account.cmd.domain.rules;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;

public class AccountRules {

    public static boolean eligibleForDelete(Account account) {
        if(account.getBalance() == 0.0) {
            return true;
        }
        return false;
    }

    public static boolean eligibleForDebitOverdraft(double accountBalance, double debitAmount) {
        if(accountBalance - debitAmount < 0.0) {
            return true;
        }
        return false;
    }

    public static boolean eligibleForDebit(Account account, double debitAmount) {
        if(account.getBalance() - debitAmount > 0.0) {
            return true;
        }
        return false;
    }
}
