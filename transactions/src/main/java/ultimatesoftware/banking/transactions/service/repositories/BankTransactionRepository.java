package ultimatesoftware.banking.transactions.service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ultimatesoftware.banking.transactions.domain.models.BankTransaction;

import java.util.List;

public interface BankTransactionRepository extends MongoRepository<BankTransaction, String> {
    List<BankTransaction> findByCustomerId(String customerId);
}
