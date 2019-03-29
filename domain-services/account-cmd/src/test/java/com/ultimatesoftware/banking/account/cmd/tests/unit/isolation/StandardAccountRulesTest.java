package com.ultimatesoftware.banking.account.cmd.tests.unit.isolation;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.rules.StandardAccountRules;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardAccountRulesTest {

    @Test
    public void givenZeroAccount_WhenDebitMaxDouble_ThenOutputShouldBeFalse() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(0));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDebit(account, maxDouble);

        // assert
        assertEquals(false, eligible);
    }

    @Test
    public void givenNegativeAccount_WhenDebit_ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(-50.0));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDebit(account, 10);

        // assert
        assertEquals(false, eligible);
    }

    @Test
    public void givenMaxNegativeAccount_WhenDebit_ThenOutputShouldBeFalse() {
        // arrange
        double minDouble = Double.MIN_VALUE;
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(minDouble));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDebit(account, 10.0);

        // assert
        assertEquals(false, eligible);
    }

    @Test
    public void givenMaxAccount_WhenDebit_ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(maxDouble));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDebit(account, 10.0);

        // assert
        assertEquals(true, eligible);
    }

    @Test
    public void given50Account_WhenDebit50_ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(50.0));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDebit(account, 50.0);

        // assert
        assertEquals(true, eligible);
    }

    @Test
    public void givenMaxAccount_WhenDebitMaxAmountMinusOne_ThenOutputShouldBeTrue() {
        // arrange
        double maxDouble = Double.MAX_VALUE;
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(maxDouble));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDebit(account, BigDecimal.valueOf(maxDouble).subtract(BigDecimal.valueOf(1)).doubleValue());

        // assert
        assertEquals(true, eligible);
    }

    @Test
    public void givenPositiveAccount_WhenDelete_ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(10.0));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDelete(account);

        // assert
        assertEquals(false, eligible);
    }

    @Test
    public void givenNegativeAccount_WhenDelete_ThenOutputShouldBeFalse() {
        // arrange
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(-10.0));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDelete(account);

        // assert
        assertEquals(false, eligible);
    }

    @Test
    public void givenZeroAccount_WhenDelete_ThenOutputShouldBeTrue() {
        // arrange
        Account account = new Account(ObjectId.get(), ObjectId.get().toHexString(), BigDecimal.valueOf(0.0));

        // act
        boolean eligible = new StandardAccountRules().eligibleForDelete(account);

        // assert
        assertEquals(true, eligible);
    }
}
