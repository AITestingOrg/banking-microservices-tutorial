package com.ultimatesoftware.banking.account.cmd.tests.unit.social;

import com.ultimatesoftware.banking.account.cmd.commands.ConcludeTransferDepositCommand;
import com.ultimatesoftware.banking.account.cmd.commands.ReleaseAccountCommand;
import com.ultimatesoftware.banking.account.cmd.commands.StartTransferWithdrawCommand;
import com.ultimatesoftware.banking.account.cmd.sagas.TransactionSaga;
import com.ultimatesoftware.banking.account.events.TransferDepositConcludedEvent;
import com.ultimatesoftware.banking.account.events.TransferTransactionStartedEvent;
import com.ultimatesoftware.banking.account.events.TransferWithdrawConcludedEvent;
import io.micronaut.test.annotation.MicronautTest;
import org.axonframework.test.saga.SagaTestFixture;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@MicronautTest
@Disabled
public class TransactionSagaTest {
    private static final String accountId = ObjectId.get().toHexString();
    private static final String destinationId = ObjectId.get().toHexString();
    private static final String transactionId = "This is a test id";
    private SagaTestFixture<TransactionSaga> sagaFixture;
    private final double amount = 100;

    @BeforeEach
    public void setUp() {
        sagaFixture = new SagaTestFixture<>(TransactionSaga.class);
    }

    @Test
    public void onTransactionStarted_startTransferDispatched() {
        sagaFixture.givenAggregate(accountId).published()
            .whenAggregate(accountId).publishes(TransferTransactionStartedEvent.builder()
                .id(accountId)
                .destinationAccountId(destinationId)
                .amount(amount)
                .transactionId(transactionId))
            .expectDispatchedCommands(new StartTransferWithdrawCommand(accountId, amount, transactionId));
    }


    @Test
    public void onTransferWithdrawConcluded_concludeCommandDispatched() {
        sagaFixture.givenAggregate(accountId).published(TransferTransactionStartedEvent.builder()
                .id(accountId)
                .destinationAccountId(destinationId)
                .amount(amount)
                .transactionId(transactionId))
            .whenAggregate(accountId).publishes(TransferWithdrawConcludedEvent.builder()
                .id(accountId)
                .balance(amount)
                .transactionId(transactionId))
            .expectDispatchedCommands(new ConcludeTransferDepositCommand(destinationId, amount, transactionId));
    }

    @Test
    public void onTransferDepositConcluded_releasesAreDispatched() throws Exception {
        sagaFixture.givenAggregate(accountId).published(TransferTransactionStartedEvent.builder()
                .id(accountId)
                .destinationAccountId(destinationId)
                .amount(amount)
                .transactionId(transactionId))
            .andThenAPublished(TransferWithdrawConcludedEvent.builder()
                .id(accountId)
                .balance(amount)
                .transactionId(transactionId))
            .whenAggregate(accountId).publishes(TransferDepositConcludedEvent.builder()
                .id(accountId)
                .balance(amount)
                .transactionId(transactionId))
            .expectDispatchedCommands(new ReleaseAccountCommand(accountId, transactionId),
                                          new ReleaseAccountCommand(destinationId, transactionId));
    }
}
