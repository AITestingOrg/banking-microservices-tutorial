package com.ultimatesoftware.banking.account.cmd.unit;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.DeleteAccountCommand;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.AccountNotEligibleForDeleteException;
import com.ultimatesoftware.banking.account.common.events.AccountDeletedEvent;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AggregateLifecycle.class)
public class AccountTest {
    private Account account;

    @Before
    public void setup() {
        mockStatic(AggregateLifecycle.class);
        when(AggregateLifecycle.apply(anyObject())).thenReturn(anyObject());
    }

    @Test
    public void givenAccountIsEligibleForDelete_WhenDeleting_DeletedEventEmitted() throws AccountNotEligibleForDeleteException {
        // arrange
        UUID uuid = UUID.randomUUID();
        account = new Account();

        // act
        account.on(new DeleteAccountCommand(uuid));

        // assert
        verifyStatic(AggregateLifecycle.class);
        AggregateLifecycle.apply(new AccountDeletedEvent(uuid));
    }
}
