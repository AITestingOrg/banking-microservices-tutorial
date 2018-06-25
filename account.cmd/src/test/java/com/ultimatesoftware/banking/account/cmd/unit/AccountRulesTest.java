package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountRulesTest {

    @Test
    public void GivenZeroAccount_WhenDebitMaxDouble__ThenOutputShouldBeFalse() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(0));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, maxDouble);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenNegativeAccount_WhenDebit__ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(-50.0));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxNegativeAccount_WhenDebit__ThenOutputShouldBeFalse() {
        // arrange
        double minDouble = Double.MIN_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(minDouble));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10.0);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxAccount_WhenDebit__ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(maxDouble));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void Given50Account_WhenDebit50__ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(50.0));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 50.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void GivenMaxAccount_WhenDebitMaxAmountMinusOne__ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(maxDouble));

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, BigDecimal.valueOf(maxDouble).subtract(BigDecimal.valueOf(1)).doubleValue());

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void GivenPositiveAccount_WhenDelete__ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(10.0));

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenNegativeAccount_WhenDelete__ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(-10.0));

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenZeroAccount_WhenDelete__ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(0.0));

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(true, eligible);
    }
}