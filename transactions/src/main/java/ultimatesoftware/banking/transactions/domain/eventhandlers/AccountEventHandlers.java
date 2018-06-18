package ultimatesoftware.banking.transactions.domain.eventhandlers;

import com.ultimatesoftware.banking.account.common.events.*;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import ultimatesoftware.banking.transactions.domain.models.TransactionType;
import ultimatesoftware.banking.transactions.service.repositories.BankTransactionRepository;

public class AccountEventHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandlers.class);

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    public AccountEventHandlers() {}

    @EventHandler
    public void on(AccountCreditedEvent event) {
        LOG.info("Account Credited {}", event.getId());
        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(event.getId())
                .setType(TransactionType.DEPOSIT)
                .setAmount(event.getCreditAmount())
                .setCustomerId(event.getCustomerId())
                .build();
        bankTransactionRepository.save(transaction);
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        LOG.info("Account Debited {}", event.getId());
        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(event.getId())
                .setType(TransactionType.WITHDRAW)
                .setAmount(event.getDebitAmount())
                .setCustomerId(event.getCustomerId())
                .build();
        bankTransactionRepository.save(transaction);
    }

    @EventHandler
    public void on(AccountOverdraftedEvent event) {
        LOG.info("Account Overdraft {}", event.getId());
        BankTransaction transaction = new BankTransaction.BankTransactionBuilder()
                .setAccount(event.getId())
                .setType(TransactionType.OVERDRAFT)
                .setAmount(event.getDebitAmount())
                .setCustomerId(event.getCustomerId())
                .build();
        bankTransactionRepository.save(transaction);
    }
}
