package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountRulesTest {

    @Test
    public void givenZeroAccount_WhenDebitMaxDouble_ThenOutputShouldBeFalse() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(0));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, maxDouble);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void givenNegativeAccount_WhenDebit_ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(-50.0));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void givenMaxNegativeAccount_WhenDebit_ThenOutputShouldBeFalse() {
        // arrange
        double minDouble = Double.MIN_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(minDouble));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10.0);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void givenMaxAccount_WhenDebit_ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(maxDouble));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void given50Account_WhenDebit50_ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(50.0));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 50.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void givenMaxAccount_WhenDebitMaxAmountMinusOne_ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(maxDouble));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, BigDecimal.valueOf(maxDouble).subtract(BigDecimal.valueOf(1)).doubleValue());

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void givenPositiveAccount_WhenDelete_ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(10.0));

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void givenNegativeAccount_WhenDelete_ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(-10.0));

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void givenZeroAccount_WhenDelete_ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(0.0));

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(true, eligible);
    }
}
