package com.ultimatesoftware.banking.account.cmd.integration;

import com.ultimatesoftware.banking.account.cmd.domain.commands.ConcludeTransferDepositCommand;
import com.ultimatesoftware.banking.account.cmd.domain.commands.ReleaseAccountCommand;
import com.ultimatesoftware.banking.account.cmd.domain.commands.StartTransferWithdrawCommand;
import com.ultimatesoftware.banking.account.cmd.domain.sagas.TransactionSaga;
import com.ultimatesoftware.banking.account.cmd.service.scheduling.FutureCommandSend;
import com.ultimatesoftware.banking.account.common.events.TransferDepositConcludedEvent;
import com.ultimatesoftware.banking.account.common.events.TransferTransactionStartedEvent;
import com.ultimatesoftware.banking.account.common.events.TransferWithdrawConcludedEvent;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations="classpath:application-test.yml")
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class TransactionSagaTest {
    private static final UUID accountId = UUID.randomUUID();
    private static final UUID destinationId = UUID.randomUUID();
    private static final String transactionId = "123e4567-e89b-12d3-a456-426655440010";
    private SagaTestFixture<TransactionSaga> sagaFixture;
    private final double amount = 100;

    @Mock
    private ScheduledFuture cancellationTimer;

    @Mock
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Before
    public void setUp() {
        sagaFixture = new SagaTestFixture<>(TransactionSaga.class);
        sagaFixture.registerResource(threadPoolTaskScheduler);
    }

    @Test
    public void onTransactionStarted_startTransferDispatched() {
        sagaFixture.givenAggregate(accountId.toString()).published()
                .whenAggregate(accountId.toString()).publishes(new TransferTransactionStartedEvent(accountId,
                                                                                                   destinationId,
                                                                                                   amount,
                                                                                                   transactionId))
                .expectDispatchedCommands(new StartTransferWithdrawCommand(accountId, amount, transactionId));
    }


    @Test
    public void onTransferWithdrawConcluded_concludeCommandDispatched() {
        // arrange
        when(threadPoolTaskScheduler.schedule(any(FutureCommandSend.class), any(Date.class)))
            .thenReturn(cancellationTimer);

        sagaFixture.givenAggregate(accountId.toString()).published(new TransferTransactionStartedEvent(accountId,
                                                                                                       destinationId,
                                                                                                       amount,
                                                                                                       transactionId))
                .whenAggregate(accountId.toString()).publishes(new TransferWithdrawConcludedEvent(accountId,
                                                                                                  amount,
                                                                                                  transactionId))
                .expectDispatchedCommands(new ConcludeTransferDepositCommand(destinationId, amount, transactionId));
    }

    @Test
    public void onTransferDepositConcluded_timerIsCanceled() throws Exception {
        // arrange
        when(threadPoolTaskScheduler.schedule(any(FutureCommandSend.class), any(Date.class)))
                .thenReturn(cancellationTimer);

        sagaFixture.givenAggregate(accountId.toString()).published(new TransferTransactionStartedEvent(accountId,
                                                                                                       destinationId,
                                                                                                       amount,
                                                                                                       transactionId))
                .andThenAPublished(new TransferWithdrawConcludedEvent(accountId, amount,transactionId))
                .whenAggregate(accountId.toString()).publishes(new TransferDepositConcludedEvent(destinationId,
                                                                                                 amount,
                                                                                                 transactionId));

        verify(cancellationTimer, times(1)).cancel(false);
    }
    @Test
    public void onTransferDepositConcluded_releasesAreDispatched() throws Exception {
        // arrange
        when(threadPoolTaskScheduler.schedule(any(FutureCommandSend.class), any(Date.class)))
                .thenReturn(cancellationTimer);

        sagaFixture.givenAggregate(accountId.toString()).published(new TransferTransactionStartedEvent(accountId,
                                                                                                       destinationId,
                                                                                                       amount,
                                                                                                       transactionId))
                .andThenAPublished(new TransferWithdrawConcludedEvent(accountId, amount,transactionId))
                .whenAggregate(accountId.toString()).publishes(new TransferDepositConcludedEvent(destinationId,
                                                                                                 amount,
                                                                                                 transactionId))
                .expectDispatchedCommands(new ReleaseAccountCommand(accountId, transactionId),
                                          new ReleaseAccountCommand(destinationId, transactionId));
    }
}
