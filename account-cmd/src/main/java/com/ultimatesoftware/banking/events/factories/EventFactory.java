package com.ultimatesoftware.banking.events.factories;

import com.ultimatesoftware.banking.events.*;

import java.util.UUID;
import org.bson.types.ObjectId;

public class EventFactory {
    public static AccountEvent createEvent(AccountEventType type, String id, String customerId, double amount, double balance, String transactionId, String msg, String destinationAccountId) throws Exception {
        switch(type) {
            case CREATED:
                return AccountCreatedEvent.builder()
                    .id(id)
                    .customerId(customerId)
                    .balance(balance)
                    .build();
            case DELETED:
                return AccountDeletedEvent.builder()
                    .id(id)
                    .build();
            case CREDITED:
                return AccountCreditedEvent.builder()
                    .id(id)
                    .customerId(customerId)
                    .balance(balance)
                    .creditAmount(amount)
                    .transactionId(transactionId)
                    .build();
            case DEBITED:
                return AccountDebitedEvent.builder()
                    .id(id)
                    .customerId(customerId)
                    .balance(balance)
                    .debitAmount(amount)
                    .transactionId(transactionId)
                    .build();
            case RELEASED:
                return AccountReleasedEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .build();
            case UPDATED:
                return AccountUpdatedEvent.builder()
                    .id(id)
                    .customerId(customerId)
                    .build();
            case TRANSACTION_FAILED:
                return TransactionFailedEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .msg(msg)
                    .build();
            case TRANSFER_CANCELLED:
                return TransferCanceledEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .balance(balance)
                    .build();
            case TRANSFER_CONCLUDED:
                return TransferDepositConcludedEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .balance(balance)
                    .build();
            case TRANSFER_FAILED_TO_START:
                return TransferFailedToStartEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .build();
            case TRANSACTION_STARTED:
                return TransferTransactionStartedEvent.builder()
                    .id(id)
                    .destinationAccountId(destinationAccountId)
                    .amount(amount)
                    .transactionId(transactionId)
                    .build();
            case TRANSFER_WITHDRAW_CONCLUDED:
                return TransferWithdrawConcludedEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .balance(balance)
                    .build();
            case TRANSFER_DEPOSIT_CONCLUDED:
                return TransferDepositConcludedEvent.builder()
                    .id(id)
                    .transactionId(transactionId)
                    .balance(balance)
                    .build();
            default:
                throw new Exception(String.format("No event of type \"%s\" exists!", type.toString()));
        }
    }

    public static AccountEvent createEvent(AccountEventType type, String id, String customerId, String transactionId, String msg) throws Exception {
        return createEvent(type, id, customerId, 0.0, 0.0, transactionId, msg, null);
    }

    public static AccountEvent createEvent(AccountEventType type, String id, String transactionId, String msg) throws Exception {
        return createEvent(type, id, null, 0.0, 0.0, transactionId, msg, null);
    }

    public static AccountEvent createEvent(AccountEventType type, String id, double balance, String transactionId) throws Exception {
        return createEvent(type, id, null, 0.0, balance, transactionId, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, String id, String customerId) throws Exception {
        return createEvent(type, id, customerId, 0.0, 0.0, null, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, String id, String customerId, double balance) throws Exception {
        return createEvent(type, id, customerId, 0.0, balance, null, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, String id, String customerId, double amount, double balance, String transactionId) throws Exception {
        return createEvent(type, id, customerId, amount, balance, transactionId, null, null);
    }

    public static AccountEvent createEvent(AccountEventType type, String id, String destinationAccount, double amount, String transactionId) throws Exception {
        return createEvent(type, id, null, amount, 0.0, transactionId, null, destinationAccount);
    }

    public static AccountEvent createEvent(AccountEventType type, String id) throws Exception {
        return createEvent(type, id, null, 0.0, 0.0, null, null, null);
    }
}
