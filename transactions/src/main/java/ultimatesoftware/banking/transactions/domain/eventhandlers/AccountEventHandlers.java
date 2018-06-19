package ultimatesoftware.banking.transactions.domain.eventhandlers;

import com.ultimatesoftware.banking.account.common.events.*;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import ultimatesoftware.banking.transactions.domain.models.TransactionStatus;
import ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

public class AccountEventHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandlers.class);

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    public AccountEventHandlers() {}

    @EventHandler
    public void on(AccountCreditedEvent event) {
        LOG.info("Account Credited {}", event.getId());
        updateTransaction(event);
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        LOG.info("Account Debited {}", event.getId());
        updateTransaction(event);
    }

    @EventHandler
    public void on(AccountOverdraftedEvent event) {
        LOG.info("Account Overdraft {}", event.getId());
        updateTransaction(event);
    }

    private void updateTransaction(AccountTransactionEvent event) {
        BankTransaction transaction = bankTransactionRepository.findOne(event.getTransactionId());
        if(transaction != null) {
            if (event.isSuccess()) {
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
            }
        }
        bankTransactionRepository.save(transaction);
    }
}
