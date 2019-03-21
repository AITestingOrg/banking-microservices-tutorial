package com.ultimatesoftware.banking.account.cmd.tests.unit.social;


public class TransactionSagaTest {
    /*
    private static final UUID accountId = UUID.randomUUID();
    private static final UUID destinationId = UUID.randomUUID();
    private static final String transactionId = "123e4567-e89b-12d3-a456-426655440010";
    private SagaTestFixture<TransactionSaga> sagaFixture;
    private final double amount = 100;

    @Mock
    private ScheduledFuture cancellationTimer;


    @BeforeEach
    public void setUp() {
        sagaFixture = new SagaTestFixture<>(TransactionSaga.class);
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
                .andThenAPublished(new TransferWithdrawConcludedEvent(accountId, amount, transactionId))
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
                .andThenAPublished(new TransferWithdrawConcludedEvent(accountId, amount, transactionId))
                .whenAggregate(accountId.toString()).publishes(new TransferDepositConcludedEvent(destinationId,
                                                                                                 amount,
                                                                                                 transactionId))
                .expectDispatchedCommands(new ReleaseAccountCommand(accountId, transactionId),
                                          new ReleaseAccountCommand(destinationId, transactionId));
    }
    */
}
