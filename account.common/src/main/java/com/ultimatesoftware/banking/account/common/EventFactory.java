package com.ultimatesoftware.banking.account.common;

import com.ultimatesoftware.banking.account.common.events.*;

import java.util.UUID;

public class EventFactory {
    public static AccountEvent createEvent(AccountEventType type, UUID id, UUID customerId, double balance, double amount, String transactionId, String msg, UUID destinationAccountId) throws Exception {
        switch(type) {
            case CREATED:
                return new AccountCreatedEvent(id, customerId, balance);
            case DELETED:
                return new AccountDeletedEvent(id);
            case CREDITED:
                return new AccountCreditedEvent(id, customerId, amount, balance, transactionId);
            case DEBITED:
                return new AccountDebitedEvent(id, customerId, amount, balance, transactionId);
            case RELEASED:
                return new AccountReleasedEvent(id, transactionId);
            case UPDATED:
                return new AccountUpdatedEvent(id, customerId);
            case DESTINATION_AQUIRED:
                return new DestinationAccountAcquiredEvent(id, transactionId);
            case SOURCE_AQUIRED:
                return new SourceAccountAcquiredEvent(id, transactionId);
            case TRANSACTION_FAILED:
                return new TransactionFailedEvent(id, transactionId, msg);
            case TRANSFER_CANCELLED:
                return new TransferCanceledEvent(id, transactionId);
            case TRANSFER_CONCLUDED:
                return new TransferDepositConcludedEvent(id, balance, transactionId);
            case TRANSFER_FAILED_TO_START:
                return new TransferFailedToStartEvent(id, transactionId);
            case TRANSFER_STARTED:
                return new TransferTransactionStartedEvent(id, destinationAccountId, amount, transactionId);
            case TRANSFER_WITHDRAW_CONCLUDED:
                return new TransferWithdrawConcludedEvent(id, balance, transactionId);
            default:
                throw new Exception(String.format("No event of type \"%s\" exists!", type.toString()));
        }
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, String transactionId) throws Exception {
        return createEvent(type, id, null, 0.0, 0.0, transactionId, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, String transactionId, String msg) throws Exception {
        return createEvent(type, id, null, 0.0, 0.0, transactionId, msg, null);
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, double balance, String transactionId) throws Exception {
        return createEvent(type, id, null, balance, 0.0, transactionId, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, UUID customerId) throws Exception {
        return createEvent(type, id, customerId, 0.0, 0.0, null, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, UUID customerId, double balance) throws Exception {
        return createEvent(type, id, customerId, balance, 0.0, null, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, UUID customerId, double balance, double amount, String transactionId) throws Exception {
        return createEvent(type, id, customerId, balance, amount, transactionId, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, UUID id, UUID destinationAccount, double amount, String transactionId) throws Exception {
        return createEvent(type, id, null, 0.0, amount, transactionId, null, destinationAccount);
    }

    public static Object createEvent(AccountEventType type, UUID id) throws Exception {
        return createEvent(type, id, null, 0.0, 0.0, null, null, null);
    }
}
