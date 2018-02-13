package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.rules.AccountRules;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class AccountRulesTest {

    @Test
    public void GivenZeroAccount_WhenDebitMaxDouble__ThenOutputShouldBeFalse() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), "t", 0, true);

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, maxDouble);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenNegativeAccount_WhenDebit__ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), "t", -50.0, true);

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxNegativeAccount_WhenDebit__ThenOutputShouldBeFalse() {
        // arrange
        double minDouble = Double.MIN_VALUE;
        Account account = new Account(UUID.randomUUID(), "t", minDouble, true);

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10.0);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxAccount_WhenDebit__ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), "t", maxDouble, true);

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 10.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void Given50Account_WhenDebit50__ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(UUID.randomUUID(), "t", 50.0, true);

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, 50.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void GivenMaxAccount_WhenDebitMaxAmountMinusOne__ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(UUID.randomUUID(), "t", maxDouble, true);

        // act
        boolean eligible = AccountRules.eligibleForDebit(account, maxDouble - 1);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void GivenPositiveAccount_WhenDelete__ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), "t", 10.0, true);

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenNegativeAccount_WhenDelete__ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(UUID.randomUUID(), "t", -10.0, true);

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenZeroAccount_WhenDelete__ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(UUID.randomUUID(), "t", 0.0, true);

        // act
        boolean eligible = AccountRules.eligibleForDelete(account);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void GivenZeroAccount_WhenOverdraftMaxDouble__ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;

        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(0.0, maxDouble);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void Given10Account_WhenOverdraft15__ThenOutputShouldBeTrue() {
        // arrange

        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(10.0, 15.0);

        // assert
        Assert.assertEquals(true, eligible);
    }

    @Test
    public void GivenNegativeAccount_WhenOverdraft__ThenOutputShouldBeFalse() {
        // arrange

        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(-10, 10);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxNegativeAccount_WhenOverdraft__ThenOutputShouldBeFalse() {
        // arrange
        double minDouble = Double.MIN_VALUE;

        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(minDouble, 10.0);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxAccount_WhenOverdraft__ThenOutputShouldBeFalse() {
        // arrange
        double maxDouble = Double.MAX_VALUE;

        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(maxDouble, 10.0);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void Given50Account_WhenOverdraft50__ThenOutputShouldBeFalse() {
        // arrange


        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(50.0, 50.0);

        // assert
        Assert.assertEquals(false, eligible);
    }

    @Test
    public void GivenMaxAccount_WhenOverdraftMaxAmountMinusOne__ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;


        // act
        boolean eligible = AccountRules.eligibleForDebitOverdraft(maxDouble, maxDouble - 1);

        // assert
        Assert.assertEquals(false, eligible);
    }
}