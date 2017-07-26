package ultimatesoftware.banking.transactions.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ultimatesoftware.banking.transactions.models.BankTransaction;

import java.util.List;

/**
 * Created by justinp on 7/26/17.
 */
public interface BankTransactionRepository extends MongoRepository<BankTransaction, String> {
    List<BankTransaction> findByCustomerId(String customerId);
}
